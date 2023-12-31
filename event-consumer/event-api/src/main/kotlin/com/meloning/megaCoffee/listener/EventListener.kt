package com.meloning.megaCoffee.listener

import com.meloning.megaCoffee.clients.javaEmail.model.EmailFormType
import com.meloning.megaCoffee.clients.javaEmail.service.EmailSender
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class EventListener(
    private val emailSender: EmailSender,
    private val mailProperties: MailProperties
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(
        bindings = [
            QueueBinding(value = Queue(), exchange = Exchange(value = "amq.topic", type = "topic", durable = "true"), key = ["email.notification"])
        ]
    )
    fun emailListener(@Payload payload: Map<String, String>) {
        logger.info("payload=$payload")
        val emailFormType = EmailFormType.valueOf(payload["type"]!!.uppercase())
        emailSender.send(emailFormType.getEmailFormDto(mailProperties.username, payload))
    }
}
