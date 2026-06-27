package com.automation.comments

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CommentsResponseAutomationApplication

fun main(args: Array<String>) {
    runApplication<CommentsResponseAutomationApplication>(*args)
}
