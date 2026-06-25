package com.automation.comments.domain.port.inbound

import com.automation.comments.domain.model.KeywordRule

interface RuleManagementUseCase {
    fun createRule(rule: KeywordRule): KeywordRule
    fun updateRule(id: Long, rule: KeywordRule): KeywordRule
    fun deleteRule(id: Long)
    fun listRules(): List<KeywordRule>
    fun getRule(id: Long): KeywordRule
}
