package com.meloning.megaCoffee.infra.database.mysql.domain.store.repository

import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.QStoreEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.StoreEntity
import com.meloning.megaCoffee.infra.database.mysql.util.asNumberExpression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest

class CustomStoreJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomStoreJpaRepository {
    private val qStoreEntity = QStoreEntity.storeEntity

    override fun findAll(storeId: Long?, page: Int, size: Int): InfiniteScrollType<StoreEntity> {
        val pageRequest = PageRequest.of(page, size)
        val result = jpaQueryFactory.selectFrom(qStoreEntity)
            .where(
                gtStoreLastId(storeId)
            )
            .orderBy(qStoreEntity.id.asc())
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

    private fun gtStoreLastId(storeLastId: Long?): BooleanExpression? {
        return storeLastId?.let { qStoreEntity.id.gt(storeLastId.asNumberExpression()) }
    }
}
