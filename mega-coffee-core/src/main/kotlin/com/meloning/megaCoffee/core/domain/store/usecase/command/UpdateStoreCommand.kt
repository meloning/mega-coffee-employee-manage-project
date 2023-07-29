package com.meloning.megaCoffee.core.domain.store.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.store.model.StoreType

data class UpdateStoreCommand(
    val type: StoreType?,
    val ownerId: Long?,
    val address: Address?,
    val timeRange: TimeRange?
)
