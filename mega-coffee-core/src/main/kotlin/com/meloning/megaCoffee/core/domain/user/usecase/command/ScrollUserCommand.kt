package com.meloning.megaCoffee.core.domain.user.usecase.command

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType

data class ScrollUserCommand(
    val userId: Long,
    val keyword: String?,
    val employeeType: EmployeeType?,
    val workTimeType: WorkTimeType?,
    val storeId: Long?
)
