package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.KeywordRuleEntity
import com.automation.comments.domain.model.KeywordRule
import com.automation.comments.domain.port.outbound.RuleRepository
import org.springframework.stereotype.Component

@Component
class RuleJpaAdapter(
    private val jpaRepository: RuleJpaRepository,
) : RuleRepository {

    override fun save(rule: KeywordRule): KeywordRule =
        jpaRepository.save(KeywordRuleEntity.fromDomain(rule)).toDomain()

    override fun findById(id: Long): KeywordRule? =
        jpaRepository.findById(id).orElse(null)?.toDomain()

    override fun findAll(): List<KeywordRule> =
        jpaRepository.findAll().map { it.toDomain() }

    override fun findEnabledByPageIdOrGlobal(pageId: String): List<KeywordRule> =
        jpaRepository.findEnabledByPageIdOrGlobal(pageId).map { it.toDomain() }

    override fun delete(id: Long) = jpaRepository.deleteById(id)
}
