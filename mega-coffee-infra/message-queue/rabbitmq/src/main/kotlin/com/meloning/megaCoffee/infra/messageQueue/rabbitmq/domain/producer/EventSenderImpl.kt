package com.meloning.megaCoffee.infra.messageQueue.rabbitmq.domain.producer

import com.meloning.megaCoffee.core.infra.event.EventSender
import com.meloning.megaCoffee.core.infra.event.EventType
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class EventSenderImpl(
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: TopicExchange
) : EventSender {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async(value = "asyncTaskExecutor")
    override fun send(type: EventType, payload: Map<String, String>) {
        logger.info("routingKey=${type.value}, payload=$payload")
        rabbitTemplate.convertAndSend(exchange.name, type.value, payload)
    }
}
