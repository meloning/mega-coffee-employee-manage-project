package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.common.extension.isNull
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.store.repository.findNotDeletedByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.usecase.command.CreateUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.UpdateUserCommand
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: IUserRepository,
    private val storeRepository: IStoreRepository,
    private val educationRepository: IEducationRepository
) {

    // 이름, 역할, 근무시간대, 근무 장소
    @Transactional(readOnly = true)
    fun scroll(command: ScrollUserCommand, page: Int, size: Int): InfiniteScrollType<Pair<User, Store>> {
        return userRepository.findAll(command, page, size)
    }

    @Transactional(readOnly = true)
    fun detail(id: Long): Triple<User, Store, List<Education>> {
        // 근무 매장, 교육 프로그램, 교육 장소정보들
        val user = userRepository.findByIdOrThrow(id)

        val store = storeRepository.findByIdOrThrow(user.storeId)
        val educations = educationRepository.findAllByStoreIdAndUserId(store.id!!, user.id!!)
        return Triple(user, store, educations)
    }

    fun create(command: CreateUserCommand): Pair<User, Store> {
        if (userRepository.existsByNameAndEmail(command.name, command.email)) {
            throw AlreadyExistException("이미 존재하는 유저입니다.")
        }

        val store = storeRepository.findNotDeletedByIdOrThrow(command.storeId)

        val newUser = userRepository.save(command.toModel(store.id!!))
        if (newUser.isOwner() && store.ownerId.isNull()) {
            store.update(ownerId = newUser.id)
        }

        return newUser to store
    }

    fun update(id: Long, command: UpdateUserCommand): User {
        val user = userRepository.findByIdOrThrow(id)

        val store = command.storeId?.let {
            storeRepository.findNotDeletedByIdOrThrow(it)
        }

        with(command) {
            user.update(address, employeeType, phoneNumber, workTimeType, store?.id)
        }

        userRepository.update(user)

        return user
    }

    fun delete(id: Long) {
        val user = userRepository.findByIdOrThrow(id)
        userRepository.deleteById(user.id!!)
    }
}
