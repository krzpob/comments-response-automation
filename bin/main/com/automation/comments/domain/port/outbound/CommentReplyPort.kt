package com.automation.comments.domain.port.outbound

interface CommentReplyPort {
    fun replyToComment(commentId: String, message: String): Boolean
}
