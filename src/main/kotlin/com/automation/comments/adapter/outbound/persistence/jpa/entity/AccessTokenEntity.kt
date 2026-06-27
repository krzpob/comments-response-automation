package com.automation.comments.adapter.outbound.persistence.jpa.entity

import com.automation.comments.domain.model.AccessToken
import com.automation.comments.domain.model.TokenType
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "access_tokens")
class AccessTokenEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "owner_ig_id", nullable = false)
    var ownerIgId: String = "",

    @Column(name = "owner_username", nullable = false)
    var ownerUsername: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var token: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant = Instant.now(),

    @Column(name = "refreshed_at")
    var refreshedAt: Instant? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    var tokenType: TokenType = TokenType.SHORT_LIVED,
) {
    fun toDomain() = AccessToken(
        id = id,
        ownerIgId = ownerIgId,
        ownerUsername = ownerUsername,
        token = token,
        createdAt = createdAt,
        expiresAt = expiresAt,
        refreshedAt = refreshedAt,
        tokenType = tokenType,
    )

    companion object {
        fun fromDomain(accessToken: AccessToken) = AccessTokenEntity(
            id = accessToken.id,
            ownerIgId = accessToken.ownerIgId,
            ownerUsername = accessToken.ownerUsername,
            token = accessToken.token,
            createdAt = accessToken.createdAt,
            expiresAt = accessToken.expiresAt,
            refreshedAt = accessToken.refreshedAt,
            tokenType = accessToken.tokenType,
        )
    }
}
