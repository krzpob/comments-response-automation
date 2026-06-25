package com.automation.comments.domain.port.outbound

interface MessagingPort {
    fun sendDirectMessage(recipientId: String, message: String): Boolean
}
