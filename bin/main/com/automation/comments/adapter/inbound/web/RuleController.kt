package com.automation.comments.adapter.inbound.web

import com.automation.comments.adapter.inbound.web.dto.RuleRequest
import com.automation.comments.adapter.inbound.web.dto.RuleResponse
import com.automation.comments.domain.port.inbound.RuleManagementUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rules")
class RuleController(
    private val ruleManagementUseCase: RuleManagementUseCase,
) {

    @GetMapping
    fun listRules(): List<RuleResponse> =
        ruleManagementUseCase.listRules().map { RuleResponse.fromDomain(it) }

    @GetMapping("/{id}")
    fun getRule(@PathVariable id: Long): RuleResponse =
        RuleResponse.fromDomain(ruleManagementUseCase.getRule(id))

    @PostMapping
    fun createRule(@Valid @RequestBody request: RuleRequest): ResponseEntity<RuleResponse> {
        val created = ruleManagementUseCase.createRule(request.toDomain())
        return ResponseEntity.status(HttpStatus.CREATED).body(RuleResponse.fromDomain(created))
    }

    @PutMapping("/{id}")
    fun updateRule(
        @PathVariable id: Long,
        @Valid @RequestBody request: RuleRequest,
    ): RuleResponse = RuleResponse.fromDomain(ruleManagementUseCase.updateRule(id, request.toDomain()))

    @DeleteMapping("/{id}")
    fun deleteRule(@PathVariable id: Long): ResponseEntity<Void> {
        ruleManagementUseCase.deleteRule(id)
        return ResponseEntity.noContent().build()
    }
}
