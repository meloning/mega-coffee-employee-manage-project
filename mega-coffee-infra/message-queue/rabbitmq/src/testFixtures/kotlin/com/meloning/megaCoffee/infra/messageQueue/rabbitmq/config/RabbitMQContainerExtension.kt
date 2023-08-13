package com.meloning.megaCoffee.infra.messageQueue.rabbitmq.config

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.LoggerFactory
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container

object RabbitMQContainerExtension : Extension, BeforeAllCallback, AfterAllCallback {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private const val RABBITMQ_IMAGE_NAME = "rabbitmq:3-management"

    @Container
    val rabbitmq: RabbitMQContainer = RabbitMQContainer(RABBITMQ_IMAGE_NAME)

    /**
     * DynamicPropertySource가 동작하지 않아 System property 주입으로 해결
     * @see https://stackoverflow.com/a/74121511
     */
    private fun initProperty() {
        System.setProperty("spring.rabbitmq.host", rabbitmq.host)
        System.setProperty("spring.rabbitmq.port", rabbitmq.amqpPort.toString())
        System.setProperty("spring.rabbitmq.username", rabbitmq.adminUsername)
        System.setProperty("spring.rabbitmq.password", rabbitmq.adminUsername)
    }

    override fun beforeAll(context: ExtensionContext) {
        if (rabbitmq.isRunning) {
            return
        }
        rabbitmq.start()
        initProperty()

        logger.info("[Test-RabbitMQ]\n" + rabbitmq.execInContainer("rabbitmqadmin", "list", "users").stdout)
    }

    override fun afterAll(context: ExtensionContext) {
        if (rabbitmq.isRunning)
            rabbitmq.stop()
    }
}
