package com.meloning.megaCoffee.domain.user.dto

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand

data class ScrollUserRequest(
    val userId: Long?,
    val keyword: String?,
    val employeeType: EmployeeType?,
    val workTimeType: WorkTimeType?,
    val storeId: Long?
) {

    fun toCommand() = ScrollUserCommand(userId, keyword, employeeType, workTimeType, storeId)
}
