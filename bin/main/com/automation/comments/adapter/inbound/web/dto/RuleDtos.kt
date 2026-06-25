package com.automation.comments.adapter.inbound.web.dto

import com.automation.comments.domain.model.KeywordRule
import jakarta.validation.constraints.NotBlank

data class RuleRequest(
    @field:NotBlank val keyword: String,
    @field:NotBlank val dmTemplate: String,
    @field:NotBlank val commentReplyTemplate: String,
    val pageId: String? = null,
    val enabled: Boolean = true,
) {
    fun toDomain() = KeywordRule(
        keyword = keyword,
        dmTemplate = dmTemplate,
        commentReplyTemplate = commentReplyTemplate,
        pageId = pageId,
        enabled = enabled,
    )
}

data class RuleResponse(
    val id: Long?,
    val keyword: String,
    val dmTemplate: String,
    val commentReplyTemplate: String,
    val pageId: String?,
    val enabled: Boolean,
) {
    companion object {
        fun fromDomain(rule: KeywordRule) = RuleResponse(
            id = rule.id,
            keyword = rule.keyword,
            dmTemplate = rule.dmTemplate,
            commentReplyTemplate = rule.commentReplyTemplate,
            pageId = rule.pageId,
            enabled = rule.enabled,
        )
    }
}
