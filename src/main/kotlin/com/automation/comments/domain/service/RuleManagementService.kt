package com.automation.comments.domain.service

import com.automation.comments.domain.model.KeywordRule
import com.automation.comments.domain.port.inbound.RuleManagementUseCase
import com.automation.comments.domain.port.outbound.RuleRepository

class RuleManagementService(
    private val ruleRepository: RuleRepository,
) : RuleManagementUseCase {

    override fun createRule(rule: KeywordRule): KeywordRule = ruleRepository.save(rule)

    override fun updateRule(id: Long, rule: KeywordRule): KeywordRule {
        ruleRepository.findById(id) ?: throw NoSuchElementException("Rule $id not found")
        return ruleRepository.save(rule.copy(id = id))
    }

    override fun deleteRule(id: Long) {
        ruleRepository.findById(id) ?: throw NoSuchElementException("Rule $id not found")
        ruleRepository.delete(id)
    }

    override fun listRules(): List<KeywordRule> = ruleRepository.findAll()

    override fun getRule(id: Long): KeywordRule =
        ruleRepository.findById(id) ?: throw NoSuchElementException("Rule $id not found")
}
