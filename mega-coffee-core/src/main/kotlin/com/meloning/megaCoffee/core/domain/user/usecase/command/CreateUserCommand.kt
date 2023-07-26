package com.meloning.megaCoffee.core.domain.user.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType

data class CreateUserCommand(
    val email: String,
    val name: Name,
    val address: Address,
    val employeeType: EmployeeType,
    val phoneNumber: PhoneNumber,
    val workTimeType: WorkTimeType,
    val storeId: Long
) {

    fun toModel(storeId: Long) = User(
        email = email,
        name = name,
        homeAddress = address,
        employeeType = employeeType,
        phoneNumber = phoneNumber,
        workTimeType = workTimeType,
        storeId = storeId
    )
}
