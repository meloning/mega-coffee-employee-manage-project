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
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {
    constructor(id: Long?, name: Name, employeeType: EmployeeType, workTimeType: WorkTimeType, storeId: Long) :
        this(id, Constant.EMPTY, name, Address.DUMMY, employeeType, PhoneNumber.DUMMY, workTimeType, storeId)

    fun isOwner(): Boolean = this.employeeType == EmployeeType.OWNER

    fun update(
        homeAddress: Address?,
        employeeType: EmployeeType?,
        phoneNumber: PhoneNumber?,
        workTimeType: WorkTimeType?,
        storeId: Long?
    ) {
        homeAddress?.let { this.homeAddress = it }
        employeeType?.let { this.employeeType = employeeType }
        phoneNumber?.let { this.phoneNumber = it }
        workTimeType?.let { this.workTimeType = it }
        storeId?.let { this.storeId = storeId }
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

    override fun toString(): String {
        return "User(id=$id, email='$email', name=$name, homeAddress=$homeAddress, employeeType=$employeeType, phoneNumber=$phoneNumber, workTimeType=$workTimeType, storeId=$storeId, deleted=$deleted, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
