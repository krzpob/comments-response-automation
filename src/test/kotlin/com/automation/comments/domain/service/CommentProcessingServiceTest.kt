package com.automation.comments.domain.service

import com.automation.comments.domain.model.KeywordRule
import com.automation.comments.domain.port.inbound.IncomingComment
import com.automation.comments.domain.port.outbound.CommentEventRepository
import com.automation.comments.domain.port.outbound.CommentReplyPort
import com.automation.comments.domain.port.outbound.MessagingPort
import com.automation.comments.domain.port.outbound.RuleRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class CommentProcessingServiceTest {

    private val ruleRepository: RuleRepository = mock()
    private val commentEventRepository: CommentEventRepository = mock {
        on { save(any()) } doAnswer { it.arguments[0] as com.automation.comments.domain.model.CommentEvent }
    }
    private val messagingPort: MessagingPort = mock()
    private val commentReplyPort: CommentReplyPort = mock()

    private val service = CommentProcessingService(
        ruleRepository, commentEventRepository, messagingPort, commentReplyPort
    )

    @Test
    fun `sends DM and replies when keyword matches and DM succeeds`() {
        val rule = KeywordRule(id = 1, keyword = "promo", dmTemplate = "Cześć! {keyword}", commentReplyTemplate = "Sprawdź DM!")
        whenever(ruleRepository.findEnabledByPageIdOrGlobal("page1")).thenReturn(listOf(rule))
        whenever(messagingPort.sendDirectMessage(any(), any())).thenReturn(true)
        whenever(commentReplyPort.replyToComment(any(), any())).thenReturn(true)

        service.handleComment(IncomingComment("c1", "user1", "page1", "media1", "chcę promo"))

        verify(messagingPort).sendDirectMessage("user1", "Cześć! promo")
        verify(commentReplyPort).replyToComment("c1", "Sprawdź DM!")
        verify(commentEventRepository).save(argThat { dmSent && commentReplied && matchedRuleId == 1L })
    }

    @Test
    fun `does not reply to comment when DM fails`() {
        val rule = KeywordRule(id = 2, keyword = "info", dmTemplate = "Info DM", commentReplyTemplate = "Sprawdź DM!")
        whenever(ruleRepository.findEnabledByPageIdOrGlobal("page1")).thenReturn(listOf(rule))
        whenever(messagingPort.sendDirectMessage(any(), any())).thenReturn(false)

        service.handleComment(IncomingComment("c2", "user2", "page1", "media1", "potrzebuję info"))

        verify(commentReplyPort, never()).replyToComment(any(), any())
        verify(commentEventRepository).save(argThat { !dmSent && !commentReplied })
    }

    @Test
    fun `saves event without rule when no keyword matches`() {
        whenever(ruleRepository.findEnabledByPageIdOrGlobal(any())).thenReturn(emptyList())

        service.handleComment(IncomingComment("c3", "user3", "page1", "media1", "zwykły komentarz"))

        verify(messagingPort, never()).sendDirectMessage(any(), any())
        verify(commentEventRepository).save(argThat { matchedRuleId == null })
    }
}
