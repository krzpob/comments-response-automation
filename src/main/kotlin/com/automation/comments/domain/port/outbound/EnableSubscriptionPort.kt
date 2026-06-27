package com.automation.comments.domain.port.outbound

import com.automation.comments.domain.model.AccessToken

interface EnableSubscriptionPort {
    fun enableSubscription(token: AccessToken)
}