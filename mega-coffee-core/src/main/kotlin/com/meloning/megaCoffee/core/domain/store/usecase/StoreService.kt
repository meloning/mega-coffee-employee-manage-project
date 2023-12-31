package com.meloning.megaCoffee.core.domain.store.usecase

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.store.usecase.command.CreateStoreCommand
import com.meloning.megaCoffee.core.domain.store.usecase.command.UpdateStoreCommand
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StoreService(
    private val storeRepository: IStoreRepository,
    private val userRepository: IUserRepository,
    private val educationRepository: IEducationRepository,
) {

    @Transactional(readOnly = true)
    fun scroll(storeId: Long?, page: Int, size: Int): InfiniteScrollType<Store> {
        return storeRepository.findAll(storeId, page, size)
    }

    @Transactional(readOnly = true)
    fun detail(id: Long): Pair<Store, List<Education>> {
        val store = storeRepository.findByIdOrThrow(id)

        // 교육 프로그램들 및 교육장소들
        val educations = educationRepository.findAllByStoreId(store.id!!)
        return store to educations
    }

    fun create(command: CreateStoreCommand): Store {
        if (storeRepository.existsByName(command.name)) {
            throw AlreadyExistException("이미 존재하는 매장입니다.")
        }

        return storeRepository.save(command.toModel())
    }

    fun update(id: Long, command: UpdateStoreCommand): Store {
        val store = storeRepository.findByIdOrThrow(id)

        val owner = command.ownerId?.let {
            userRepository.findByIdOrThrow(it)
        }

        with(command) {
            store.update(type, owner?.id, address, timeRange)
        }

        storeRepository.update(store)

        return store
    }

    fun delete(id: Long) {
        val store = storeRepository.findByIdOrThrow(id)
        storeRepository.deleteById(store.id!!)
    }
}
