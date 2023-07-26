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
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {
    constructor(id: Long?, name: Name, employeeType: EmployeeType, workTimeType: WorkTimeType, storeId: Long) :
        this(id, Constant.EMPTY, name, Address.DUMMY, employeeType, PhoneNumber.DUMMY, workTimeType, storeId)

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
