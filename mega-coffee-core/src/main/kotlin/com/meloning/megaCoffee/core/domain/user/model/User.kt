package com.meloning.megaCoffee.core.domain.user.model

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
    // 근무 매장
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {

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
