package com.automation.comments.config

import com.automation.comments.domain.port.outbound.CommentEventRepository
import com.automation.comments.domain.port.outbound.CommentReplyPort
import com.automation.comments.domain.port.outbound.MessagingPort
import com.automation.comments.domain.port.outbound.RuleRepository
import com.automation.comments.domain.service.CommentProcessingService
import com.automation.comments.domain.service.RuleManagementService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun commentProcessingService(
        ruleRepository: RuleRepository,
        commentEventRepository: CommentEventRepository,
        messagingPort: MessagingPort,
        commentReplyPort: CommentReplyPort,
    ) = CommentProcessingService(ruleRepository, commentEventRepository, messagingPort, commentReplyPort)

    @Bean
    fun ruleManagementService(ruleRepository: RuleRepository) = RuleManagementService(ruleRepository)
}
