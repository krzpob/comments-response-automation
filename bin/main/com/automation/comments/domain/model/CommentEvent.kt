package com.automation.comments.domain.model

import java.time.Instant

data class CommentEvent(
    val id: Long? = null,
    val commentId: String,
    val senderId: String,
    val pageId: String,
    val mediaId: String,
    val commentText: String,
    val matchedRuleId: Long? = null,
    val dmSent: Boolean = false,
    val commentReplied: Boolean = false,
    val processedAt: Instant = Instant.now(),
)
