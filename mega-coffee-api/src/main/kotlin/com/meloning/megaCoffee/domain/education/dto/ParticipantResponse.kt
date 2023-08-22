package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.user.model.User

data class ParticipantResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    companion object {
        @JvmStatic
        fun from(model: User) = with(model) {
            ParticipantResponse(
                id = id!!,
                name = name.value,
                email = email
            )
        }
    }
}
