package com.automation.comments.domain.model

import java.util.UUID
import java.time.Instant

data class AccessToken(
    val id: UUID = UUID.randomUUID(),
    val ownerIgId:String,
    val ownerUsername:String,
    val token:String,
    val createdAt:Instant = Instant.now(),
    val expiresAt:Instant,
    val refreshedAt:Instant? = null,
    val tokenType: TokenType
){
    fun isExpired() = Instant.now().isAfter(expiresAt)
    
}

enum class TokenType(){
    SHORT_LIVED,LONG_LIVED
}