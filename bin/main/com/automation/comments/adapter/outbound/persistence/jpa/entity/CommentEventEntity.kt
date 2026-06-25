package com.automation.comments.adapter.outbound.persistence.jpa.entity

import com.automation.comments.domain.model.CommentEvent
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "comment_events")
class CommentEventEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "comment_id", nullable = false)
    var commentId: String = "",

    @Column(name = "sender_id", nullable = false)
    var senderId: String = "",

    @Column(name = "page_id", nullable = false)
    var pageId: String = "",

    @Column(name = "media_id", nullable = false)
    var mediaId: String = "",

    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    var commentText: String = "",

    @Column(name = "matched_rule_id")
    var matchedRuleId: Long? = null,

    @Column(name = "dm_sent", nullable = false)
    var dmSent: Boolean = false,

    @Column(name = "comment_replied", nullable = false)
    var commentReplied: Boolean = false,

    @Column(name = "processed_at", nullable = false)
    var processedAt: Instant = Instant.now(),
) {
    fun toDomain() = CommentEvent(
        id = id,
        commentId = commentId,
        senderId = senderId,
        pageId = pageId,
        mediaId = mediaId,
        commentText = commentText,
        matchedRuleId = matchedRuleId,
        dmSent = dmSent,
        commentReplied = commentReplied,
        processedAt = processedAt,
    )

    companion object {
        fun fromDomain(event: CommentEvent) = CommentEventEntity(
            id = event.id,
            commentId = event.commentId,
            senderId = event.senderId,
            pageId = event.pageId,
            mediaId = event.mediaId,
            commentText = event.commentText,
            matchedRuleId = event.matchedRuleId,
            dmSent = event.dmSent,
            commentReplied = event.commentReplied,
            processedAt = event.processedAt,
        )
    }
}
