package com.automation.comments.domain.port.outbound

import com.automation.comments.domain.model.MessageButton

interface MessagingPort {
    fun sendDirectMessage(recipientId: String, message: String, button: MessageButton? = null): Boolean
}
