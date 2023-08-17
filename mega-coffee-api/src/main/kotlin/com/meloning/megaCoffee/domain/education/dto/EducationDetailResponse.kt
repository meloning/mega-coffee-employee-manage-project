package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.domain.common.dto.EducationPlaceRow

data class EducationDetailResponse(
    val id: Long,
    val name: String,
    val content: String,
    val targetTypes: List<EmployeeType>,
    val educationPlaces: List<EducationPlaceRow>,
    val createdAt: String?,
    val updatedAt: String?
) {

    companion object {
        @JvmStatic
        fun from(model: Education) = with(model) {
            EducationDetailResponse(
                id = id!!,
                name = name.value,
                content = content,
                targetTypes = targetTypes.toList(),
                educationPlaces = educationPlaces.value.map { EducationPlaceRow.from(it) },
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
