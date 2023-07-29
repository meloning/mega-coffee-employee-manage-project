package com.meloning.megaCoffee.domain.store.dto

import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.usecase.command.UpdateStoreCommand
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest

data class UpdateStoreRequest(
    val type: StoreType?,
    val ownerId: Long?,
    val address: AddressRequest?,
    val timeRange: TimeRangeRequest?
) {

    fun toCommand() = UpdateStoreCommand(type, ownerId, address?.toModel(), timeRange?.toModel())
}
