package com.automation.comments.domain.model

data class KeywordRule(
    val id: Long? = null,
    val keyword: String,
    val dmTemplate: String,
    val commentReplyTemplate: String,
    val pageId: String? = null,
    val enabled: Boolean = true,
    val dmButton: MessageButton? = null,
)
