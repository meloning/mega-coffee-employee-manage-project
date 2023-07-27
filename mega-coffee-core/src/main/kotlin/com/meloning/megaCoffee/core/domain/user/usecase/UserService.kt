package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.usecase.command.CreateUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.UpdateUserCommand
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: IUserRepository,
    private val storeRepository: IStoreRepository
) {

    // 이름, 역할, 근무시간대, 근무 장소
    @Transactional(readOnly = true)
    fun list(command: ScrollUserCommand, page: Int, size: Int): InfiniteScrollType<Pair<User, Store>> {
        return userRepository.findAll(command, page, size)
    }

    @Transactional(readOnly = true)
    fun detail(id: Long) {
        // 근무 매장, 교육 프로그램, 교육 장소정보들
        val user = userRepository.findByIdOrThrow(id)

        storeRepository.findByIdOrThrow(user.storeId)
        // TODO: 내가 들어야할 교육 프로그램들과 교육 장소
    }

    fun create(command: CreateUserCommand): Pair<User, Store> {
        if (userRepository.existsByNameAndEmail(command.name, command.email)) {
            throw RuntimeException("이미 존재하는 유저입니다.")
        }

        val store = storeRepository.findByIdOrThrow(command.storeId)

        return userRepository.save(command.toModel(store.id!!)) to store
    }

    fun update(id: Long, command: UpdateUserCommand): User {
        val user = userRepository.findByIdOrThrow(id)

        storeRepository.findByIdOrThrow(id)

        with(command) {
            user.update(address, employeeType, phoneNumber, workTimeType, storeId)
        }

        return user
    }

    fun delete(id: Long) {
        val user = userRepository.findByIdOrThrow(id)
        userRepository.deleteById(user.id!!)
    }
}
