package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType

data class ScrollUserResponse(
    val id: Long,
    val name: String,
    val employeeType: EmployeeType,
    val workTimeType: WorkTimeType,
    val store: StoreSimpleResponse
) {
    data class StoreSimpleResponse(
        val id: Long,
        val name: String,
        val type: StoreType
    ) {
        companion object {
            @JvmStatic
            fun from(model: Store) = with(model) {
                StoreSimpleResponse(
                    id = id!!,
                    name = name.value,
                    type = type
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(model: User, store: Store) = with(model) {
            ScrollUserResponse(
                id = id!!,
                name = name.value,
                employeeType = employeeType,
                workTimeType = workTimeType,
                store = StoreSimpleResponse.from(store)
            )
        }
    }
}
