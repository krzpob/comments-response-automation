package com.automation.comments.domain.service

import com.automation.comments.domain.model.CommentEvent
import com.automation.comments.domain.port.inbound.CommentWebhookUseCase
import com.automation.comments.domain.port.inbound.IncomingComment
import com.automation.comments.domain.port.outbound.CommentEventRepository
import com.automation.comments.domain.port.outbound.CommentReplyPort
import com.automation.comments.domain.port.outbound.MessagingPort
import com.automation.comments.domain.port.outbound.RuleRepository
import org.slf4j.LoggerFactory

class CommentProcessingService(
    private val ruleRepository: RuleRepository,
    private val commentEventRepository: CommentEventRepository,
    private val messagingPort: MessagingPort,
    private val commentReplyPort: CommentReplyPort,
) : CommentWebhookUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleComment(event: IncomingComment) {
        val rules = ruleRepository.findEnabledByPageIdOrGlobal(event.pageId)
        val matched = rules.firstOrNull { rule ->
            event.text.contains(rule.keyword, ignoreCase = true)
        }

        if (matched == null) {
            log.debug("No matching rule for comment {} on page {}", event.commentId, event.pageId)
            commentEventRepository.save(CommentEvent(
                commentId = event.commentId,
                senderId = event.senderId,
                pageId = event.pageId,
                mediaId = event.mediaId,
                commentText = event.text,
            ))
            return
        }

        log.info("Rule [{}] matched comment {}", matched.id, event.commentId)

        val dmMessage = matched.dmTemplate.replace("{keyword}", matched.keyword)
        val dmSent = messagingPort.sendDirectMessage(event.senderId, dmMessage)

        val commentReplied = if (dmSent) {
            val replyMessage = matched.commentReplyTemplate.replace("{keyword}", matched.keyword)
            commentReplyPort.replyToComment(event.commentId, replyMessage)
        } else {
            log.warn("DM failed for sender {}, skipping comment reply", event.senderId)
            false
        }

        commentEventRepository.save(CommentEvent(
            commentId = event.commentId,
            senderId = event.senderId,
            pageId = event.pageId,
            mediaId = event.mediaId,
            commentText = event.text,
            matchedRuleId = matched.id,
            dmSent = dmSent,
            commentReplied = commentReplied,
        ))
    }
}
