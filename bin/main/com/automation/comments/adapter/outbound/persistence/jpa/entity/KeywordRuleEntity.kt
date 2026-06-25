package com.automation.comments.adapter.outbound.persistence.jpa.entity

import com.automation.comments.domain.model.KeywordRule
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
) {
    fun toDomain() = KeywordRule(
        id = id,
        keyword = keyword,
        dmTemplate = dmTemplate,
        commentReplyTemplate = commentReplyTemplate,
        pageId = pageId,
        enabled = enabled,
    )

    companion object {
        fun fromDomain(rule: KeywordRule) = KeywordRuleEntity(
            id = rule.id,
            keyword = rule.keyword,
            dmTemplate = rule.dmTemplate,
            commentReplyTemplate = rule.commentReplyTemplate,
            pageId = rule.pageId,
            enabled = rule.enabled,
        )
    }
}
