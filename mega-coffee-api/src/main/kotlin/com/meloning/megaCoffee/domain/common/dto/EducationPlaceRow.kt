package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.education.model.EducationPlace

data class EducationPlaceRow(
    val id: Long,
    val address: AddressResponse,
    val maxParticipant: Int,
    val date: String,
    val timeRange: TimeRangeResponse
) {
    companion object {
        @JvmStatic
        fun from(model: EducationPlace) = with(model) {
            EducationPlaceRow(
                id = id!!,
                address = AddressResponse.from(address),
                maxParticipant = maxParticipant,
                date = date.toString(),
                timeRange = TimeRangeResponse.from(timeRange)
            )
        }
    }
}
