package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.QStoreEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.QUserEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.repository.dto.QUserShortRow
import com.meloning.megaCoffee.infra.database.mysql.domain.user.repository.dto.QUserShortRow_UserStoreRow
import com.meloning.megaCoffee.infra.database.mysql.domain.user.repository.dto.UserShortRow
import com.meloning.megaCoffee.infra.database.mysql.util.asNumberExpression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest

class CustomUserJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomUserJpaRepository {
    private val qUserEntity = QUserEntity.userEntity
    private val qStoreEntity = QStoreEntity.storeEntity

    override fun scroll(command: ScrollUserCommand, page: Int, size: Int): InfiniteScrollType<UserShortRow> {
        val pageRequest = PageRequest.of(page, size)
        val result = jpaQueryFactory.select(
            QUserShortRow(
                id = qUserEntity.id,
                name = qUserEntity.name,
                employeeType = qUserEntity.employeeType,
                workTimeType = qUserEntity.workTimeType,
                store = QUserShortRow_UserStoreRow(
                    id = qStoreEntity.id,
                    name = qStoreEntity.name,
                    type = qStoreEntity.type,
                    deleted = qStoreEntity.deleted
                )
            )
        )
            .from(qUserEntity)
            .join(qStoreEntity).on(qUserEntity.storeId.eq(qStoreEntity.id))
            .where(
                qUserEntity.deleted.isFalse,
                gtUserLastId(command.userId),
                filterStoreId(command.storeId),
                filterEmployeeType(command.employeeType),
                searchKeyword(command.keyword),
                filterWorkTimeType(command.workTimeType)
            )
            .orderBy(qUserEntity.id.asc())
            .limit(pageRequest.pageSize.toLong() + 1)
            .fetch()
            .toMutableList()

        var hasNext = false
        if (result.size > size) {
            result.removeAt(size)
            hasNext = true
        }

        return result to hasNext
    }

    private fun gtUserLastId(userLastId: Long?): BooleanExpression? {
        return userLastId?.let { qUserEntity.id.gt(it.asNumberExpression()) }
    }

    private fun filterStoreId(storeId: Long?): BooleanExpression? {
        return storeId?.let { qUserEntity.storeId.eq(it) }
    }

    private fun filterEmployeeType(employeeType: EmployeeType?): BooleanExpression? {
        return employeeType?.let { qUserEntity.employeeType.eq(it) }
    }

    private fun searchKeyword(keyword: String?): BooleanExpression? {
        return keyword?.let { qUserEntity.name.name.contains(it) }
    }

    private fun filterWorkTimeType(workTimeType: WorkTimeType?): BooleanExpression? {
        return workTimeType?.let { qUserEntity.workTimeType.eq(it) }
    }
}
