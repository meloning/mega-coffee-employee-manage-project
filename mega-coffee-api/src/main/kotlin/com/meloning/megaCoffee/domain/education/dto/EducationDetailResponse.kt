package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.domain.common.dto.EducationAddressRow

data class EducationDetailResponse(
    val id: Long,
    val name: String,
    val content: String,
    val targetTypes: List<EmployeeType>,
    val educationAddresses: List<EducationAddressRow>,
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
                educationAddresses = educationAddresses.value.map { EducationAddressRow.from(it) },
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
