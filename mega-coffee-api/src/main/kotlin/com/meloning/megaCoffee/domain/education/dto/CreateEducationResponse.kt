package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType

data class CreateEducationResponse(
    val id: Long,
    val name: String,
    val content: String,
    val targetTypes: List<EmployeeType>,
    val createdAt: String?,
    val updatedAt: String?
) {
    companion object {
        @JvmStatic
        fun from(model: Education) = with(model) {
            CreateEducationResponse(
                id = id!!,
                name = name.value,
                content = content,
                targetTypes = targetTypes.toList(),
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
