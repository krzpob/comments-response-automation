package com.automation.comments.domain.port.outbound

import com.automation.comments.domain.model.KeywordRule

interface RuleRepository {
    fun save(rule: KeywordRule): KeywordRule
    fun findById(id: Long): KeywordRule?
    fun findAll(): List<KeywordRule>
    fun findEnabledByPageIdOrGlobal(pageId: String): List<KeywordRule>
    fun delete(id: Long)
}
