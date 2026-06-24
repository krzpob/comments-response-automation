package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.CommentEventEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentEventJpaRepository : JpaRepository<CommentEventEntity, Long>
