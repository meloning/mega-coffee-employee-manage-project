package com.meloning.megaCoffee.infra.database.mysql.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.support.RetryTemplate

@Configuration
@EnableRetry
class RetryConfig {

    @Bean
    fun retryTemplate(): RetryTemplate {
        return RetryTemplate.builder()
            .fixedBackoff(BACK_OFF_INTERVAL)
            .maxAttempts(MAX_ATTEMPTS)
            .build()
    }

    companion object {
        const val BACK_OFF_INTERVAL = 5000L // milliseconds
        const val MAX_ATTEMPTS = 3
    }
}
