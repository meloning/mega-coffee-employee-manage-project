package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.command.CreateUserCommand
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class CreateUserRequest(
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val name: String,
    @field:Valid
    val address: AddressRequest,
    val employeeType: EmployeeType,
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val phoneNumber: String,
    val workTimeType: WorkTimeType,
    @field:Positive(message = "양수여야 합니다.")
    val storeId: Long
) {

    fun toCommand() = CreateUserCommand(email, Name(name), address.toModel(), employeeType, PhoneNumber(phoneNumber), workTimeType, storeId)
}
