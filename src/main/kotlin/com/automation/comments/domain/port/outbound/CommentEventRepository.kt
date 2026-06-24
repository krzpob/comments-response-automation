package com.automation.comments.domain.port.outbound

import com.automation.comments.domain.model.CommentEvent

interface CommentEventRepository {
    fun save(event: CommentEvent): CommentEvent
}
