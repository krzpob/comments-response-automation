package com.automation.comments.adapter.inbound.web.dto

import com.fasterxml.jackson.annotation.JsonProperty

// Meta Webhook payload structure
data class MetaWebhookPayload(
    val `object`: String,
    val entry: List<Entry>,
) {
    data class Entry(
        val id: String,
        val changes: List<Change>,
    )

    data class Change(
        val field: String,
        val value: ChangeValue,
    )

    data class ChangeValue(
        @JsonProperty("from") val from: From?,
        @JsonProperty("comment_id") val commentId: String?,
        @JsonProperty("media") val media: Media?,
        @JsonProperty("message") val message: String?,
        @JsonProperty("parent_id") val parentId: String?,
    )

    data class From(val id: String, val name: String?)
    data class Media(val id: String)
}
