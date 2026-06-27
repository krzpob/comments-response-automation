package com.automation.comments.adapter.outbound.meta

import com.automation.comments.adapter.outbound.meta.dto.PostCommentRequest
import com.automation.comments.adapter.outbound.meta.dto.SendMessageRequest
import com.automation.comments.domain.model.AccessToken
import com.automation.comments.domain.model.MessageButton
import com.automation.comments.domain.port.outbound.CommentReplyPort
import com.automation.comments.domain.port.outbound.EnableSubscriptionPort
import com.automation.comments.domain.port.outbound.MessagingPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class MetaGraphApiAdapter(
    val metaProperties: MetaProperties
) : MessagingPort, CommentReplyPort, EnableSubscriptionPort {

    companion object {
        private val log = LoggerFactory.getLogger(javaClass)
        private const val SUBSCRIBE_FIELD ="comments,messaging_handover,messaging_postbacks"
    }


    private val baseUrl = "https://graph.facebook.com"

    private val client: RestClient = RestClient.builder()
        .baseUrl(baseUrl)
        .build()

    override fun sendDirectMessage(recipientId: String, message: String, button: MessageButton?): Boolean {
        return try {
            val response = client.post()
                .uri("/${metaProperties.apiVersion}/me/messages?access_token=${metaProperties.pageAccessToken}")
                .body(
                    SendMessageRequest(
                        recipient = SendMessageRequest.Recipient(recipientId),
                        message = if (button != null) {
                            val attachment = SendMessageRequest.Attachment(
                                type = "template",
                                payload = SendMessageRequest.TemplatePayload(
                                    template_type = "button",
                                    text = message,
                                    buttons = listOf(
                                        SendMessageRequest.Button(
                                            type = button.type,
                                            title = button.title,
                                            payload = button.payload
                                        )
                                    )
                                )
                            )
                            SendMessageRequest.MessageBody(text = message, attachment = attachment)
                        } else {
                            SendMessageRequest.MessageBody(text = message)
                        },
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
                .uri("/${metaProperties.apiVersion}/$commentId/comments?access_token=${metaProperties.pageAccessToken}")
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

    override fun enableSubscription(token: AccessToken) {
        try {
            val url = "/${metaProperties.apiVersion}/me/subscribed_apps?subscribed_fields=$SUBSCRIBE_FIELD" +
                    "&access_token=${metaProperties.appToken}"
            log.info("Enabled subscription for $url")
            val response = client.post()
                .uri("/${metaProperties.apiVersion}/me/subscribed_apps?subscribed_fields=$SUBSCRIBE_FIELD" +
                        "&access_token=${metaProperties.appToken}")
            .retrieve()
                .body<EnableSubscriptionResponseDTO>()
            if (response?.success==true) log.info("Subscription enabled")
        } catch (ex: Exception){
            log.error("Failed to enable subscription: {}", ex.message)
        }
    }
}

data class EnableSubscriptionResponseDTO(val success: Boolean)
