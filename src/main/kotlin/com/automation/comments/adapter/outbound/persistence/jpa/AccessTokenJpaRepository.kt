package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.AccessTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenJpaRepository : JpaRepository<AccessTokenEntity, java.util.UUID> {
    fun findByOwnerIgId(ownerIgId: String): AccessTokenEntity?
}
