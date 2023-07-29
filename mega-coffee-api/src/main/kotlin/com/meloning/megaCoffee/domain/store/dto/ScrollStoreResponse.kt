package com.meloning.megaCoffee.domain.store.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType

data class ScrollStoreResponse(
    val id: Long,
    val name: String,
    val type: StoreType
) {

    companion object {
        @JvmStatic
        fun from(model: Store) = with(model) {
            ScrollStoreResponse(
                id = id!!,
                name = name.value,
                type = type
            )
        }
    }
}
