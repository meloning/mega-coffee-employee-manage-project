package com.meloning.megaCoffee.core.domain.store.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.exception.NotFoundException
import com.meloning.megaCoffee.core.util.InfiniteScrollType

interface IStoreRepository {
    fun save(store: Store): Store
    fun saveAll(stores: List<Store>): List<Store>
    fun update(store: Store)

    fun findNotDeletedById(id: Long): Store?
    fun findById(id: Long): Store?
    fun findAllById(ids: List<Long>): List<Store>
    fun findAll(storeId: Long?, page: Int, size: Int): InfiniteScrollType<Store>

    fun existsByName(name: Name): Boolean

    fun deleteById(id: Long)
}

fun IStoreRepository.findNotDeletedByIdOrThrow(id: Long): Store =
    this.findNotDeletedById(id) ?: throw NotFoundException("매장이 존재하지 않습니다.")

fun IStoreRepository.findByIdOrThrow(id: Long): Store =
    this.findById(id) ?: throw NotFoundException("매장이 존재하지 않습니다.")
