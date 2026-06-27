package com.automation.comments.adapter.inbound.web

import com.automation.comments.adapter.outbound.meta.MetaGraphApiAdapter
import com.automation.comments.domain.port.outbound.AccessTokenRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SubscriptionController (val instagramClient: MetaGraphApiAdapter, val tokenRepository: AccessTokenRepository){
    @PostMapping("/subscriptions")
    fun enable(): ResponseEntity<String> {

        val token = tokenRepository.fetchByOwnerId("27485943631026209")
        instagramClient.enableSubscription(token)
        return ResponseEntity.ok("Success")
    }
}