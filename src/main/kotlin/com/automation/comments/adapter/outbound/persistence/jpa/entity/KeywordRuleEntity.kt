package com.automation.comments.adapter.outbound.persistence.jpa.entity

import com.automation.comments.domain.model.KeywordRule
import com.automation.comments.domain.model.MessageButton
import jakarta.persistence.*

@Entity
@Table(name = "keyword_rules")
class KeywordRuleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var keyword: String = "",

    @Column(name = "dm_template", nullable = false, columnDefinition = "TEXT")
    var dmTemplate: String = "",

    @Column(name = "comment_reply_template", nullable = false, columnDefinition = "TEXT")
    var commentReplyTemplate: String = "",

    @Column(name = "page_id")
    var pageId: String? = null,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @Column(name = "dm_button_type")
    var dmButtonType: String? = null,

    @Column(name = "dm_button_title")
    var dmButtonTitle: String? = null,

    @Column(name = "dm_button_payload")
    var dmButtonPayload: String? = null,
) {
    fun toDomain(): KeywordRule {
        val buttonType = dmButtonType
        val buttonTitle = dmButtonTitle
        val buttonPayload = dmButtonPayload

        return KeywordRule(
            id = id,
            keyword = keyword,
            dmTemplate = dmTemplate,
            commentReplyTemplate = commentReplyTemplate,
            pageId = pageId,
            enabled = enabled,
            dmButton = if (buttonType != null && buttonTitle != null && buttonPayload != null) {
                MessageButton(buttonType, buttonTitle, buttonPayload)
            } else {
                null
            },
        )
    }

    companion object {
        fun fromDomain(rule: KeywordRule) = KeywordRuleEntity(
            id = rule.id,
            keyword = rule.keyword,
            dmTemplate = rule.dmTemplate,
            commentReplyTemplate = rule.commentReplyTemplate,
            pageId = rule.pageId,
            enabled = rule.enabled,
            dmButtonType = rule.dmButton?.type,
            dmButtonTitle = rule.dmButton?.title,
            dmButtonPayload = rule.dmButton?.payload,
        )
    }
}
