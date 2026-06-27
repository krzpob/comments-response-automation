package com.automation.comments.domain.port.inbound

interface CommentWebhookUseCase {
    fun handleComment(event: IncomingComment)
    fun handlePostback(event: IncomingPostback)
}

data class IncomingComment(
    val commentId: String,
    val senderId: String,
    val pageId: String,
    val mediaId: String,
    val text: String,
)

data class IncomingPostback(
    val senderId: String,
    val pageId: String,
    val payload: String,
)
