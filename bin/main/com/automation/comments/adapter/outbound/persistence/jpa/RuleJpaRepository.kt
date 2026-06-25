package com.automation.comments.adapter.outbound.persistence.jpa

import com.automation.comments.adapter.outbound.persistence.jpa.entity.KeywordRuleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RuleJpaRepository : JpaRepository<KeywordRuleEntity, Long> {

    @Query("""
        SELECT r FROM KeywordRuleEntity r
        WHERE r.enabled = true
          AND (r.pageId = :pageId OR r.pageId IS NULL)
    """)
    fun findEnabledByPageIdOrGlobal(pageId: String): List<KeywordRuleEntity>
}
