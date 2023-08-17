package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.domain.common.dto.AddressResponse
import com.meloning.megaCoffee.domain.common.dto.CommonStoreResponse
import com.meloning.megaCoffee.domain.common.dto.EducationPlaceRow

data class UserDetailResponse(
    val id: Long,
    val email: String,
    val name: String,
    val address: AddressResponse,
    val employeeType: EmployeeType,
    val phoneNumber: String,
    val workTimeType: WorkTimeType,
    val store: CommonStoreResponse,
    val educations: List<EducationResponse>,
    val createdAt: String?,
    val updatedAt: String?
) {

    data class EducationResponse(
        val id: Long,
        val name: String,
        val educationPlaces: List<EducationPlaceRow>
    ) {
        companion object {
            @JvmStatic
            fun from(model: Education) = with(model) {
                EducationResponse(
                    id = id!!,
                    name = name.value,
                    educationPlaces = educationPlaces.value.map { EducationPlaceRow.from(it) }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(model: User, store: Store, educations: List<Education>) = with(model) {
            UserDetailResponse(
                id = id!!,
                email = email,
                name = name.value,
                address = AddressResponse.from(homeAddress),
                employeeType = employeeType,
                phoneNumber = phoneNumber.phone,
                workTimeType = workTimeType,
                store = CommonStoreResponse.from(store),
                educations = educations.map { EducationResponse.from(it) },
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
