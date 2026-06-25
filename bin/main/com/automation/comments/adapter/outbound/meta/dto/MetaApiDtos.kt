package com.automation.comments.adapter.outbound.meta.dto

data class SendMessageRequest(
    val recipient: Recipient,
    val message: MessageBody,
    val messaging_type: String = "RESPONSE",
) {
    data class Recipient(val id: String)
    data class MessageBody(val text: String)
}

data class PostCommentRequest(val message: String)

data class MetaApiError(
    val error: ErrorBody?,
) {
    data class ErrorBody(val message: String, val code: Int)
}
