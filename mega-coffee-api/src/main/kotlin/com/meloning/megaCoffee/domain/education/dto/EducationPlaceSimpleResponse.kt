package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.education.model.EducationPlace

data class EducationPlaceSimpleResponse(
    val id: Long,
    val educationName: String,
    val address: String,
    val date: String,
    val time: String,
    val maxParticipant: Int,
    val currentParticipant: Int,
    val createdAt: String?,
    val updatedAt: String?
) {

    companion object {
        @JvmStatic
        fun from(model: EducationPlace) = with(model) {
            EducationPlaceSimpleResponse(
                id = id!!,
                educationName = education.name.value,
                address = address.getAddress(),
                date = date.toString(),
                time = timeRange.toString(),
                maxParticipant = maxParticipant,
                currentParticipant = currentParticipant,
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
