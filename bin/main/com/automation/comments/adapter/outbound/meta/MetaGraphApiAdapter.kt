package com.automation.comments.adapter.outbound.meta

import com.automation.comments.adapter.outbound.meta.dto.PostCommentRequest
import com.automation.comments.adapter.outbound.meta.dto.SendMessageRequest
import com.automation.comments.domain.port.outbound.CommentReplyPort
import com.automation.comments.domain.port.outbound.MessagingPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MetaGraphApiAdapter(
    @Value("\${meta.page-access-token}") private val pageAccessToken: String,
    @Value("\${meta.api-version:v19.0}") private val apiVersion: String,
) : MessagingPort, CommentReplyPort {

    private val log = LoggerFactory.getLogger(javaClass)
    private val baseUrl = "https://graph.facebook.com"

    private val client: RestClient = RestClient.builder()
        .baseUrl(baseUrl)
        .build()

    override fun sendDirectMessage(recipientId: String, message: String): Boolean {
        return try {
            val response = client.post()
                .uri("/$apiVersion/me/messages?access_token=$pageAccessToken")
                .body(
                    SendMessageRequest(
                        recipient = SendMessageRequest.Recipient(recipientId),
                        message = SendMessageRequest.MessageBody(message),
                    )
                )
                .retrieve()
                .toBodilessEntity()

            val success = response.statusCode == HttpStatus.OK
            if (!success) log.warn("DM to {} returned status {}", recipientId, response.statusCode)
            success
        } catch (ex: Exception) {
            log.error("Failed to send DM to {}: {}", recipientId, ex.message)
            false
        }
    }

    override fun replyToComment(commentId: String, message: String): Boolean {
        return try {
            val response = client.post()
                .uri("/$apiVersion/$commentId/comments?access_token=$pageAccessToken")
                .body(PostCommentRequest(message))
                .retrieve()
                .toBodilessEntity()

            val success = response.statusCode == HttpStatus.OK
            if (!success) log.warn("Comment reply on {} returned status {}", commentId, response.statusCode)
            success
        } catch (ex: Exception) {
            log.error("Failed to reply to comment {}: {}", commentId, ex.message)
            false
        }
    }
}
