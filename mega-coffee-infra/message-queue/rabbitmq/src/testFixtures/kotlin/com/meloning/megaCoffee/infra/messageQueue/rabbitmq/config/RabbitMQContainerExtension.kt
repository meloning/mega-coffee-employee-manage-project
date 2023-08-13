package com.meloning.megaCoffee.infra.messageQueue.rabbitmq.config

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container

class RabbitMQContainerExtension : Extension, BeforeAllCallback, AfterAllCallback {
    companion object {
        private const val RABBITMQ_IMAGE_NAME = "rabbitmq:3-management"

        @JvmStatic
        @Container
        val rabbitmq: RabbitMQContainer = RabbitMQContainer(RABBITMQ_IMAGE_NAME)

        /**
         * DynamicPropertySource가 동작하지 않아 System property 주입으로 해결
         * @see https://stackoverflow.com/a/74121511
         */
        init {
            System.setProperty("spring.rabbitmq.host", rabbitmq.host)
            System.setProperty("spring.rabbitmq.port", rabbitmq.exposedPorts[0].toString())
            System.setProperty("spring.rabbitmq.username", rabbitmq.adminUsername)
            System.setProperty("spring.rabbitmq.password", rabbitmq.adminPassword)
        }
    }

    override fun beforeAll(context: ExtensionContext) {
        if (rabbitmq.isRunning) {
            return
        }
        rabbitmq.start()
    }

    override fun afterAll(context: ExtensionContext) {
        if (rabbitmq.isRunning)
            rabbitmq.stop()
    }
}
