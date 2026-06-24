package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.CommentEventEntity
import com.automation.comments.domain.model.CommentEvent
import com.automation.comments.domain.port.outbound.CommentEventRepository
import org.springframework.stereotype.Component

@Component
class CommentEventJpaAdapter(
    private val jpaRepository: CommentEventJpaRepository,
) : CommentEventRepository {

    override fun save(event: CommentEvent): CommentEvent =
        jpaRepository.save(CommentEventEntity.fromDomain(event)).toDomain()
}
