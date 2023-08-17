package com.meloning.megaCoffee.domain.store.dto

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.domain.common.dto.AddressResponse
import com.meloning.megaCoffee.domain.common.dto.EducationPlaceRow
import com.meloning.megaCoffee.domain.common.dto.TimeRangeResponse

data class StoreDetailResponse(
    val id: Long,
    val name: String,
    val type: StoreType,
    val ownerId: Long?,
    val address: AddressResponse,
    val timeRange: TimeRangeResponse,
    val educations: List<EducationRow>,
    val createdAt: String?,
    val updatedAt: String?
) {

    data class EducationRow(
        val id: Long,
        val name: String,
        val targetTypes: List<EmployeeType>,
        val educationPlaces: List<EducationPlaceRow>
    ) {
        companion object {
            @JvmStatic
            fun from(model: Education) = with(model) {
                EducationRow(
                    id = id!!,
                    name = name.value,
                    targetTypes = targetTypes.toList(),
                    educationPlaces = educationPlaces.value.map { EducationPlaceRow.from(it) }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(model: Store, educations: List<Education>) = with(model) {
            StoreDetailResponse(
                id = id!!,
                name = name.value,
                type = type,
                ownerId = ownerId,
                address = AddressResponse.from(address),
                timeRange = TimeRangeResponse.from(timeRange),
                educations = educations.map { EducationRow.from(it) },
                createdAt = createdAt?.toString(),
                updatedAt = updatedAt?.toString()
            )
        }
    }
}
