package com.meloning.megaCoffee.domain.store.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.domain.common.dto.AddressResponse
import com.meloning.megaCoffee.domain.common.dto.TimeRangeResponse

data class CreateStoreResponse(
    val id: Long,
    val name: String,
    val type: StoreType,
    val address: AddressResponse,
    val timeRange: TimeRangeResponse,
    val createdAt: String?,
    val updatedAt: String?
) {
    companion object {
        @JvmStatic
        fun from(model: Store) = with(model) {
            CreateStoreResponse(
                id = id!!,
                name = name.value,
                type = type,
                address = AddressResponse.from(address),
                timeRange = TimeRangeResponse.from(timeRange),
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
