package com.meloning.megaCoffee.listener

import com.meloning.megaCoffee.clients.javaEmail.service.EmailFormGenerator
import com.meloning.megaCoffee.clients.javaEmail.service.EmailSender
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class EventListener(
    private val emailSender: EmailSender
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(
        bindings = [
            QueueBinding(value = Queue(), exchange = Exchange(value = "amq.topic", type = "topic", durable = "true"), key = ["email.notification"])
        ]
    )
    fun emailListener(@Payload payload: Map<String, String>) {
        logger.info("payload=$payload")
        emailSender.send(EmailFormGenerator.generate(payload))
    }
}
