package com.meloning.megaCoffee.core.domain.education.usecase.command

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType

data class CreateEducationCommand(
    val name: Name,
    val content: String,
    val targetTypes: MutableList<EmployeeType>,
) {

    fun toModel() = Education(
        name = name,
        content = content,
        targetTypes = targetTypes,
        educationAddresses = EducationAddresses(mutableListOf()),
    )
}
