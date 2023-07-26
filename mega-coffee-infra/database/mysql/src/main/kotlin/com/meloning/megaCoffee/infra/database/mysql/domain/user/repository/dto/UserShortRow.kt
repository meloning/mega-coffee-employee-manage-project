package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository.dto

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.querydsl.core.annotations.QueryProjection

data class UserShortRow
@QueryProjection
constructor(
    val id: Long,
    val name: NameVO,
    val employeeType: EmployeeType,
    val workTimeType: WorkTimeType,
    val store: UserStoreRow
) {

    data class UserStoreRow
    @QueryProjection
    constructor(
        val id: Long,
        val name: NameVO,
        val type: StoreType
    ) {
        fun toModel() = Store(
            id = id,
            name = name.toModel(),
            type = type
        )
    }

    fun toModel() = User(
        id = id,
        name = name.toModel(),
        employeeType = employeeType,
        workTimeType = workTimeType,
        storeId = store.id
    )
}
