package com.meloning.megaCoffee.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("spring.rabbitmq")
data class RabbitMqProperties
@ConstructorBinding
constructor(
    val host: String,
    val port: Int,
    val username: String,
    val password: String
)
