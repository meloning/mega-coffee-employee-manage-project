package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.domain.common.dto.AddressResponse
import com.meloning.megaCoffee.domain.common.dto.CommonStoreResponse

data class CreateUserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val address: AddressResponse,
    val employeeType: EmployeeType,
    val phoneNumber: String,
    val workTimeType: WorkTimeType,
    val store: CommonStoreResponse,
    val createdAt: String?,
    val updatedAt: String?
) {

    companion object {
        @JvmStatic
        fun from(model: User, store: Store) = with(model) {
            CreateUserResponse(
                id = id!!,
                email = email,
                name = name.value,
                address = AddressResponse.from(homeAddress),
                employeeType = employeeType,
                phoneNumber = phoneNumber.phone,
                workTimeType = workTimeType,
                store = CommonStoreResponse.from(store),
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
