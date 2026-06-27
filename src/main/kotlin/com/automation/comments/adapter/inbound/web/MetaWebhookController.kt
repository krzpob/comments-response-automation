package com.automation.comments.adapter.inbound.web

import com.automation.comments.adapter.inbound.web.dto.MetaWebhookPayload
import com.automation.comments.domain.port.inbound.CommentWebhookUseCase
import com.automation.comments.domain.port.inbound.IncomingComment
import com.automation.comments.domain.port.inbound.IncomingPostback
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.jackson.module.kotlin.jacksonObjectMapper

@RestController
@RequestMapping("/webhook")
class MetaWebhookController(
    private val commentWebhookUseCase: CommentWebhookUseCase,
    @Value("\${meta.webhook-verify-token}") private val verifyToken: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)
        private val objectMapper = jacksonObjectMapper()
    // Meta webhook verification (GET)
    @GetMapping
    fun verify(
        @RequestParam("hub.mode") mode: String,
        @RequestParam("hub.verify_token") token: String,
        @RequestParam("hub.challenge") challenge: String,
    ): ResponseEntity<String> {
        return if (mode == "subscribe" && token == verifyToken) {
            log.info("Webhook verified")
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.status(403).build()
        }
    }

    // Meta webhook events (POST)
    @PostMapping
    fun receive(@RequestBody payloadString: String): ResponseEntity<Void> {
        log.info("Receiving webhook payload: $payloadString")
        val payload = objectMapper.readValue(payloadString, MetaWebhookPayload::class.java)
        if (payload.`object` != "instagram" && payload.`object` != "page") {
            return ResponseEntity.ok().build()
        }
        log.info("Receive webhook object: {}", payload.`object`)
        
        payload.entry.forEach { entry ->
            entry.changes.forEach { change ->
                when (change.field) {
                    "comments" -> {
                        val v = change.value
                        if (v.commentId != null && v.from != null && v.text != null && v.media != null) {
                            commentWebhookUseCase.handleComment(
                                IncomingComment(
                                    commentId = v.commentId,
                                    senderId = v.from.id,
                                    pageId = entry.id,
                                    mediaId = v.media.id,
                                    text = v.text,
                                )
                            )
                        }
                    }

                    "messaging_postbacks" -> {
                        val v = change.value
                        if (v.sender != null && v.postback != null) {
                            commentWebhookUseCase.handlePostback(
                                IncomingPostback(
                                    senderId = v.sender.id,
                                    pageId = entry.id,
                                    payload = v.postback.payload,
                                )
                            )
                        }
                    }
                }
            }
        }

        return ResponseEntity.ok().build()
    }
}
