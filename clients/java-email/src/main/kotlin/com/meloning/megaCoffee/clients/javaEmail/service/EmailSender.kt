package com.meloning.megaCoffee.clients.javaEmail.service

import com.meloning.megaCoffee.clients.javaEmail.service.dto.EmailFormDto
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class EmailSender(
    private val javaMailSender: JavaMailSender
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun send(emailFormDto: EmailFormDto) {
        try {
            val message = javaMailSender.createMimeMessage()
            MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
            ).apply {
                setTo(emailFormDto.to)
                setText(emailFormDto.text, true)
                setSubject(emailFormDto.subject)
                setFrom(emailFormDto.from)
            }

            javaMailSender.send(message)
            logger.info("${Thread.currentThread().name} - Send Email Success for ${emailFormDto.to}")
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw RuntimeException("Failed to send email.", ex)
        }
    }
}
