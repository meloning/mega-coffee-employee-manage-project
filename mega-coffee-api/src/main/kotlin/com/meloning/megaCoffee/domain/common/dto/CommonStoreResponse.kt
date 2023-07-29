package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType

data class CommonStoreResponse(
    val id: Long,
    val name: String,
    val type: StoreType,
    val address: AddressResponse,
    val timeRange: TimeRangeResponse
) {
    companion object {
        @JvmStatic
        fun from(model: Store) = with(model) {
            CommonStoreResponse(
                id = id!!,
                name = name.value,
                type = type,
                address = AddressResponse.from(address),
                timeRange = TimeRangeResponse.from(timeRange)
            )
        }
    }
}
