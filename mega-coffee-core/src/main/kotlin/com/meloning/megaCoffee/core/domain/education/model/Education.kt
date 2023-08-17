package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.exception.ConflictFieldException
import com.meloning.megaCoffee.core.exception.NotRegisterException
import java.time.Instant

data class Education(
    var id: Long? = null,
    val name: Name,
    var content: String,
    var targetTypes: MutableList<EmployeeType> = mutableListOf(),
    var educationPlaces: EducationPlaces = EducationPlaces(mutableListOf()),
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {

    fun update(educationPlaces: EducationPlaces) {
        this.educationPlaces = educationPlaces
    }

    fun addAddress(educationPlace: EducationPlace) {
        this.educationPlaces.add(educationPlace)
    }

    fun removeAddress(educationPlace: EducationPlace) {
        this.educationPlaces.remove(educationPlace)
    }

    fun validateUserEligibility(userEmployeeType: EmployeeType) {
        if (!targetTypes.contains(userEmployeeType)) {
            throw ConflictFieldException("해당 유저는 ${name.value} 교육 프로그램 대상자가 아닙니다.")
        }
    }

    fun validateStoreEligibility(requiredEducationsByStore: List<Long>, storeName: String) {
        if (!requiredEducationsByStore.contains(id)) {
            throw NotRegisterException("$storeName 매장의 직원은 ${name.value}을 들을 수 없습니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Education

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
