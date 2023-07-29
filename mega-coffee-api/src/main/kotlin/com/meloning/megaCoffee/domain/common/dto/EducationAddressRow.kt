package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.education.model.EducationAddress

data class EducationAddressRow(
    val id: Long,
    val address: AddressResponse,
    val maxParticipant: Int,
    val date: String,
    val timeRange: TimeRangeResponse
) {
    companion object {
        @JvmStatic
        fun from(model: EducationAddress) = with(model) {
            EducationAddressRow(
                id = id!!,
                address = AddressResponse.from(address),
                maxParticipant = maxParticipant,
                date = date.toString(),
                timeRange = TimeRangeResponse.from(timeRange)
            )
        }
    }
}
