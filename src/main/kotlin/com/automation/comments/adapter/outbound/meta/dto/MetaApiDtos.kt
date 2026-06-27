package com.automation.comments.adapter.outbound.meta.dto

data class SendMessageRequest(
    val recipient: Recipient,
    val message: MessageBody,
    val messaging_type: String = "RESPONSE",
) {
    data class Recipient(val id: String)
    data class MessageBody(
        val text: String? = null,
        val attachment: Attachment? = null,
    )

    data class Attachment(
        val type: String,
        val payload: TemplatePayload,
    )

    data class TemplatePayload(
        val template_type: String,
        val text: String,
        val buttons: List<Button>,
    )

    data class Button(
        val type: String,
        val title: String,
        val payload: String,
    )
}

data class PostCommentRequest(val message: String)

data class MetaApiError(
    val error: ErrorBody?,
) {
    data class ErrorBody(val message: String, val code: Int)
}
