package com.meloning.megaCoffee.core.domain.user.usecase.command

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType

data class UpdateUserCommand(
    val address: Address?,
    val employeeType: EmployeeType?,
    val phoneNumber: PhoneNumber?,
    val workTimeType: WorkTimeType?,
    val storeId: Long?
)
