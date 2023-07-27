package com.meloning.megaCoffee.infra.database.mysql.domain.store.repository

import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.StoreEntity

interface CustomStoreJpaRepository {
    fun findAll(storeId: Long?, page: Int, size: Int): InfiniteScrollType<StoreEntity>
}
