package com.meloning.megaCoffee.domain.store.dto

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.usecase.command.CreateStoreCommand
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank

data class CreateStoreRequest(
    @field:NotBlank(message = "비어있거나 공백일 수 없습니다.")
    val name: String,
    val type: StoreType,
    @field:Valid
    val address: AddressRequest,
    @field:Valid
    val timeRange: TimeRangeRequest
) {

    fun toCommand() = CreateStoreCommand(Name(name), type, address.toModel(), timeRange.toModel())
}
