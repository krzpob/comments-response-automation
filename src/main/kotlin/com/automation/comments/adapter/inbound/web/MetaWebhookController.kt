package com.automation.comments.adapter.inbound.web

import com.automation.comments.adapter.inbound.web.dto.MetaWebhookPayload
import com.automation.comments.domain.port.inbound.CommentWebhookUseCase
import com.automation.comments.domain.port.inbound.IncomingComment
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/webhook")
class MetaWebhookController(
    private val commentWebhookUseCase: CommentWebhookUseCase,
    @Value("\${meta.webhook-verify-token}") private val verifyToken: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)

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
    fun receive(@RequestBody payload: MetaWebhookPayload): ResponseEntity<Void> {
        if (payload.`object` != "instagram" && payload.`object` != "page") {
            return ResponseEntity.ok().build()
        }

        payload.entry.forEach { entry ->
            entry.changes
                .filter { it.field == "comments" }
                .forEach { change ->
                    val v = change.value
                    if (v.commentId != null && v.from != null && v.message != null && v.media != null) {
                        commentWebhookUseCase.handleComment(
                            IncomingComment(
                                commentId = v.commentId,
                                senderId = v.from.id,
                                pageId = entry.id,
                                mediaId = v.media.id,
                                text = v.message,
                            )
                        )
                    }
                }
        }

        return ResponseEntity.ok().build()
    }
}
