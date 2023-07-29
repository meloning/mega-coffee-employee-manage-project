package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.user.usecase.command.RegisterEducationAddressCommand
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class RegisterEducationAddressRequest(
    @field:Positive(message = "양수여야 합니다.")
    val educationId: Long,
    @field:Size(min = 1, max = 5, message = "등록할 장소는 최소 1개에서 최대 5개까지 등록할 수 있습니다.")
    val educationAddressIds: List<Long>
) {

    fun toCommand() = RegisterEducationAddressCommand(educationId, educationAddressIds)
}
