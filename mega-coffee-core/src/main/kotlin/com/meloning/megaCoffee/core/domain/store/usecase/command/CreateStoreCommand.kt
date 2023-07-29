package com.meloning.megaCoffee.core.domain.store.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType

data class CreateStoreCommand(
    val name: Name,
    val type: StoreType,
    val address: Address,
    val timeRange: TimeRange
) {

    fun toModel() = Store(
        name = name,
        type = type,
        ownerId = null,
        address = address,
        timeRange = timeRange
    )
}
