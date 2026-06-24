package com.automation.comments.domain.model

private val URL_PATTERN = Regex("""https?://\S+|www\.\S+""", RegexOption.IGNORE_CASE)

data class KeywordRule(
    val id: Long? = null,
    val keyword: String,
    val dmTemplate: String,
    val commentReplyTemplate: String,
    val pageId: String? = null,
    val enabled: Boolean = true,
) {
    init {
        require(keyword.isNotBlank()) { "Keyword must not be blank" }
        require(dmTemplate.isNotBlank()) { "DM template must not be blank" }
        require(!URL_PATTERN.containsMatchIn(dmTemplate)) {
            "DM template must not contain URLs — Meta policy forbids links in the first message"
        }
        require(commentReplyTemplate.isNotBlank()) { "Comment reply template must not be blank" }
    }
}
