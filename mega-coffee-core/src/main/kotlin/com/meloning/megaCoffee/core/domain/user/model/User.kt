package com.meloning.megaCoffee.core.domain.user.model

import com.meloning.megaCoffee.common.constant.Constant
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import java.time.Instant

data class User(
    val id: Long? = null,
    var email: String,
    var name: Name,
    var homeAddress: Address,
    var employeeType: EmployeeType,
    var phoneNumber: PhoneNumber,
    var workTimeType: WorkTimeType,
    var storeId: Long,
    var deleted: Boolean = false,
    var educationAddressRelations: MutableList<UserEducationAddressRelation> = mutableListOf(),
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {
    constructor(id: Long?, name: Name, employeeType: EmployeeType, workTimeType: WorkTimeType, storeId: Long) :
        this(id, Constant.EMPTY, name, Address.DUMMY, employeeType, PhoneNumber.DUMMY, workTimeType, storeId)

    fun update(educationAddressRelations: MutableList<UserEducationAddressRelation>) {
        this.educationAddressRelations = educationAddressRelations
    }

    fun addEducationAddress(educationAddressId: Long) {
        educationAddressRelations.add(
            UserEducationAddressRelation(user = this, educationAddressId = educationAddressId)
        )
    }

    fun removeEducationAddress(educationAddressId: Long) {
        educationAddressRelations.removeIf { it.educationAddressId == educationAddressId }
    }

    private fun filterByContainedIds(educationAddressIds: List<Long>): List<UserEducationAddressRelation> {
        return educationAddressRelations.filter { educationAddressIds.contains(it.id) }
    }

    fun validateExistingEducationAddress(educationAddressIds: List<Long>) {
        val duplicatedValues = filterByContainedIds(educationAddressIds.map { it })
        if (duplicatedValues.isNotEmpty()) {
            throw RuntimeException("이미 등록한 교육장소를 신청하였습니다. 재등록 바랍니다.")
        }
    }

    fun update(
        homeAddress: Address,
        employeeType: EmployeeType,
        phoneNumber: PhoneNumber,
        workTimeType: WorkTimeType,
        storeId: Long
    ) {
        this.homeAddress = homeAddress
        this.employeeType = employeeType
        this.phoneNumber = phoneNumber
        this.workTimeType = workTimeType
        this.storeId = storeId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
