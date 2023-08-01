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
import com.meloning.megaCoffee.core.event.EventSender
import com.meloning.megaCoffee.core.event.EventType
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
    private val eventSender: EventSender
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

    fun registerForEducation(id: Long, educationIds: List<Long>) {
        val store = storeRepository.findByIdOrThrow(id)

        val educations = educationIds.map {
            educationRepository.findByIdOrThrow(it)
        }

        educations.forEach {
            store.addEducation(it.id!!)
        }

        storeRepository.update(store)

        // 교육 프로그램명을 매장 내 교육 대상자들에게 알림을 발송
        val targetUsersInStore = userRepository.findByStoreId(store.id!!)

        educations.forEach { education ->
            val targetUsersToNotify = targetUsersInStore.filter { education.targetTypes.contains(it.employeeType) }
            targetUsersToNotify.forEach {
                eventSender.send(
                    type = EventType.EMAIL,
                    payload = mapOf(
                        "email" to it.email,
                        "username" to it.name.value,
                        "educationName" to education.name.value,
                        "type" to "notify_user_education"
                    )
                )
            }
        }
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
