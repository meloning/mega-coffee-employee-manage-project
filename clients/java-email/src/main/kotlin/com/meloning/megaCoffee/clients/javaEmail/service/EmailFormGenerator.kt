package com.meloning.megaCoffee.clients.javaEmail.service

import com.meloning.megaCoffee.clients.javaEmail.service.dto.EmailFormDto
import org.springframework.util.ResourceUtils

object EmailFormGenerator {

    // TODO: Refactoring Target
    fun generate(payload: Map<String, String>): EmailFormDto {
        val type = payload["type"]!!
        return when (type) {
            "complete_user_education" -> {
                val subject = "교육 프로그램 신청 완료"
                val file = ResourceUtils.getFile("classpath:template/complete_user_education.html")
                val html = file.readText().replace("#{user.name}", payload["username"]!!)
                    .replace("#{education.name}", payload["educationName"]!!)
                    .replace("#{address}", payload["educationAddress"]!!)
                    .replace("#{date}", payload["date"]!!)
                    .replace("#{time}", payload["time"]!!)

                EmailFormDto(subject, html, "melon8372@gmail.com", payload["email"]!!)
            }
            else -> throw RuntimeException("지원하지 않는 타입($type)입니다.")
        }
    }
}
