package com.meloning.megaCoffee.infra.database.mysql.domain.store.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.StoreEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class StoreRepositoryImpl(
    private val storeJpaRepository: StoreJpaRepository
) : IStoreRepository {
    override fun save(store: Store): Store {
        return storeJpaRepository.save(StoreEntity.from(store)).toModel()
    }

    override fun saveAll(stores: List<Store>): List<Store> {
        return storeJpaRepository.saveAll(stores.map { StoreEntity.from(it) }).map { it.toModel() }
    }

    override fun update(store: Store) {
        storeJpaRepository.save(
            StoreEntity.from(store)
        )
    }

    override fun findAll(storeId: Long?, page: Int, size: Int): InfiniteScrollType<Store> {
        val (content, hasNext) = storeJpaRepository.findAll(storeId, page, size)
        return content.map { it.toModel() } to hasNext
    }

    override fun findById(id: Long): Store? {
        return storeJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun existsByName(name: Name): Boolean {
        return storeJpaRepository.existsByName(NameVO.from(name))
    }

    override fun deleteById(id: Long) {
        storeJpaRepository.deleteById(id)
    }
}
