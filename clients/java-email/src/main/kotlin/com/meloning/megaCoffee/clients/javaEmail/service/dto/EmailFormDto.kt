package com.meloning.megaCoffee.clients.javaEmail.service.dto

data class EmailFormDto(
    val subject: String,
    val text: String,
    val from: String,
    val to: String
)
