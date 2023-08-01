package com.meloning.megaCoffee.clients.javaEmail.model

import com.meloning.megaCoffee.clients.javaEmail.service.dto.EmailFormDto
import org.springframework.util.ResourceUtils

enum class EmailFormType(
    val subject: String
) {
    COMPLETE_USER_EDUCATION("교육 프로그램 신청 완료") {
        override fun getEmailFormDto(from: String, payload: Map<String, String>): EmailFormDto {
            val file = ResourceUtils.getFile("classpath:template/complete_user_education.html")
            val html = file.readText().replace("#{user.name}", payload["username"]!!)
                .replace("#{education.name}", payload["educationName"]!!)
                .replace("#{address}", payload["educationAddress"]!!)
                .replace("#{date}", payload["date"]!!)
                .replace("#{time}", payload["time"]!!)

            return EmailFormDto(this.subject, html, from, payload["email"]!!)
        }
    },
    NOTIFY_USER_EDUCATION("교육 프로그램 수강 대상자") {
        override fun getEmailFormDto(from: String, payload: Map<String, String>): EmailFormDto {
            val file = ResourceUtils.getFile("classpath:template/notify_user_education.html")
            val html = file.readText().replace("#{user.name}", payload["username"]!!)
                .replace("#{education.name}", payload["educationName"]!!)

            return EmailFormDto(this.subject, html, from, payload["email"]!!)
        }
    };

    abstract fun getEmailFormDto(from: String, payload: Map<String, String>): EmailFormDto
}
