package com.meloning.megaCoffee.core.domain.education.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import java.time.LocalDate

data class RegisterEducationAddressCommand(
    val addresses: MutableList<EducationAddressItem>
) {

    data class EducationAddressItem(
        val address: Address,
        val maxParticipant: Int,
        val date: LocalDate,
        val timeRange: TimeRange
    ) {
        fun toModel(education: Education) = EducationAddress(
            education = education,
            address = address,
            maxParticipant = maxParticipant,
            date = date,
            timeRange = timeRange
        )
    }

    fun toModel(education: Education) =
        EducationAddresses(addresses.map { it.toModel(education) }.toMutableList())
}
