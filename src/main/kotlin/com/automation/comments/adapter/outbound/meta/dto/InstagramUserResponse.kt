package com.automation.comments.adapter.outbound.meta.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramUserResponse(
    val id: String,
    val username: String
) {}

