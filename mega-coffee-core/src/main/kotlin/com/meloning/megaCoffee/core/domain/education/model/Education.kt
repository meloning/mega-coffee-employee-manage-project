package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import java.time.Instant

data class Education(
    var id: Long? = null,
    val name: Name,
    var content: String,
    var targetTypes: MutableList<EmployeeType> = mutableListOf(),
    var educationAddresses: EducationAddresses,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {

    fun update(educationAddresses: EducationAddresses) {
        this.educationAddresses = educationAddresses
    }

    fun addAddress(educationAddress: EducationAddress) {
        this.educationAddresses.add(educationAddress)
    }

    fun removeAddress(educationAddress: EducationAddress) {
        this.educationAddresses.remove(educationAddress)
    }

    fun validateUserEligibility(userEmployeeType: EmployeeType) {
        if (!targetTypes.contains(userEmployeeType)) {
            throw RuntimeException("해당 유저는 ${name.value} 교육 프로그램 대상자가 아닙니다.")
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
