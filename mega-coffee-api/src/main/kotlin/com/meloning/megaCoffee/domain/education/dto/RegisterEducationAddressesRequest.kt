package com.meloning.megaCoffee.domain.education.dto

import com.meloning.megaCoffee.core.domain.education.usecase.command.RegisterEducationAddressesCommand
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class RegisterEducationAddressesRequest(
    @field:Valid
    @field:Size(min = 1, max = 5, message = "최소 1개에서 최대 5개까지 등록할 수 있습니다.")
    val addresses: List<EducationAddressRequest>
) {

    data class EducationAddressRequest(
        @field:Valid
        val address: AddressRequest,
        @field:Positive(message = "양수여야 합니다.")
        val maxParticipant: Int,
        val date: String,
        @field:Valid
        val timeRange: TimeRangeRequest
    ) {

        fun toItem() = RegisterEducationAddressesCommand.EducationAddressItem(address.toModel(), maxParticipant, LocalDate.parse(date), timeRange.toModel())
    }

    fun toCommand() = RegisterEducationAddressesCommand(addresses.map { it.toItem() }.toMutableList())
}
