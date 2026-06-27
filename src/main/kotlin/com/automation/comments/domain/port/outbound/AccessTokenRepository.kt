package com.automation.comments.domain.port.outbound

import com.automation.comments.domain.model.AccessToken

interface AccessTokenRepository {
    fun save(accessToken: AccessToken): AccessToken
    
    fun fetchByOwnerId(ownerId: String): AccessToken
}
