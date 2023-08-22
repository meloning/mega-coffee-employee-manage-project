package com.meloning.megaCoffee.config

import com.meloning.megaCoffee.infra.api.EmployeeManageApiErrorHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfig {
    @Bean
    fun employeeManageRestTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .requestFactory { BufferingClientHttpRequestFactory(HttpComponentsClientHttpRequestFactory()) }
            .errorHandler(EmployeeManageApiErrorHandler())
            .defaultMessageConverters()
            .setConnectTimeout(Duration.ofMinutes(1))
            .additionalInterceptors(RestTemplateLoggingInterceptor())
            .build()
    }

    companion object {
        const val EMPLOYEE_MANAGE_API_URL = "http://localhost:8080"
    }
}
