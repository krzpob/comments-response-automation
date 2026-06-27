package com.automation.comments.adapter.inbound.web.dto

import com.automation.comments.domain.model.KeywordRule
import com.automation.comments.domain.model.MessageButton
import jakarta.validation.constraints.NotBlank

data class RuleRequest(
    @field:NotBlank val keyword: String,
    @field:NotBlank val dmTemplate: String,
    @field:NotBlank val commentReplyTemplate: String,
    val pageId: String? = null,
    val enabled: Boolean = true,
    val dmButton: MessageButtonRequest? = null,
) {
    fun toDomain() = KeywordRule(
        keyword = keyword,
        dmTemplate = dmTemplate,
        commentReplyTemplate = commentReplyTemplate,
        pageId = pageId,
        enabled = enabled,
        dmButton = dmButton?.toDomain(),
    )
}

data class RuleResponse(
    val id: Long?,
    val keyword: String,
    val dmTemplate: String,
    val commentReplyTemplate: String,
    val pageId: String?,
    val enabled: Boolean,
    val dmButton: MessageButtonResponse? = null,
) {
    companion object {
        fun fromDomain(rule: KeywordRule) = RuleResponse(
            id = rule.id,
            keyword = rule.keyword,
            dmTemplate = rule.dmTemplate,
            commentReplyTemplate = rule.commentReplyTemplate,
            pageId = rule.pageId,
            enabled = rule.enabled,
            dmButton = rule.dmButton?.let { MessageButtonResponse.fromDomain(it) },
        )
    }
}

data class MessageButtonRequest(
    val type: String,
    val title: String,
    val payload: String,
) {
    fun toDomain() = MessageButton(type = type, title = title, payload = payload)
}

data class MessageButtonResponse(
    val type: String,
    val title: String,
    val payload: String,
) {
    companion object {
        fun fromDomain(button: MessageButton) = MessageButtonResponse(
            type = button.type,
            title = button.title,
            payload = button.payload,
        )
    }
}
