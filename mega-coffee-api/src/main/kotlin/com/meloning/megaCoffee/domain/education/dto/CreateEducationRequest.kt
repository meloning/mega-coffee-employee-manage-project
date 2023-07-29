package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.usecase.command.CreateEducationCommand
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateEducationRequest(
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val name: String,
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val content: String,
    @field:Size(min = 1, message = "최소 1개 이상 존재해야 합니다.")
    val targetTypes: List<EmployeeType>
) {

    fun toCommand() = CreateEducationCommand(Name(name), content, targetTypes.toMutableList())
}
