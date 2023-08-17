package com.meloning.megaCoffee.core.domain.education.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces
import java.time.LocalDate

data class RegisterEducationPlacesCommand(
    val places: MutableList<EducationPlaceItem>
) {

    data class EducationPlaceItem(
        val address: Address,
        val maxParticipant: Int,
        val date: LocalDate,
        val timeRange: TimeRange
    ) {
        fun toModel(education: Education) = EducationPlace(
            education = education,
            address = address,
            maxParticipant = maxParticipant,
            date = date,
            timeRange = timeRange
        )
    }

    fun toModel(education: Education) =
        EducationPlaces(places.map { it.toModel(education) }.toMutableList())
}
