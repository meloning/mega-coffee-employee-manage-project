package com.meloning.megaCoffee.infra.database.mysql.domain.store.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.StoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StoreJpaRepository : JpaRepository<StoreEntity, Long>, CustomStoreJpaRepository {
    fun existsByName(name: NameVO): Boolean
}
