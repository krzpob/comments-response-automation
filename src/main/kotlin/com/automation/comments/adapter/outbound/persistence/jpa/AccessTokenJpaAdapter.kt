package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.AccessTokenEntity
import com.automation.comments.domain.model.AccessToken
import com.automation.comments.domain.port.outbound.AccessTokenRepository
import org.springframework.stereotype.Component

@Component
class AccessTokenJpaAdapter(
    private val jpaRepository: AccessTokenJpaRepository,
) : AccessTokenRepository {

    override fun save(accessToken: AccessToken): AccessToken =
        jpaRepository.save(AccessTokenEntity.fromDomain(accessToken)).toDomain()

    override fun fetchByOwnerId(ownerId: String): AccessToken =
        jpaRepository.findByOwnerIgId(ownerId)
            ?.toDomain()
            ?: throw NoSuchElementException("AccessToken for owner $ownerId not found")
}
