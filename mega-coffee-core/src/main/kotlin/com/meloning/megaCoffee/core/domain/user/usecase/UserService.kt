package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.repository.findDetailByIdOrThrow
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.repository.findDetailByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.usecase.command.CreateUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.RegisterEducationAddressCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.UpdateUserCommand
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

    fun registerEducationAddress(id: Long, command: RegisterEducationAddressCommand) {
        val user = userRepository.findDetailByIdOrThrow(id)
        val store = storeRepository.findByIdOrThrow(user.storeId)
        val education = educationRepository.findDetailByIdOrThrow(command.educationId)
        val educationAddresses = education.educationAddresses

        // [Validate]
        store.validateEligibility(education.id!!, education.name.value)
        education.validateUserEligibility(user.employeeType)

        // 새로 등록할 것들중 비교
        educationAddresses.validateExisting(command.educationAddressIds)

        val selectedEducationAddresses = educationAddresses.filterByContainedIds(command.educationAddressIds)
        val userEducationAddresses = educationAddresses.filterByContainedIds(user.educationAddressRelations.map { it.educationAddressId })

        // 기존 등록한 것들과 비교
        user.validateExistingEducationAddress(selectedEducationAddresses.map { it.id!! })

        // 같은날, 겹치는 시간대가 있는지
        UserEducationAddressValidator.validateDuplicateTimeSlots(selectedEducationAddresses + userEducationAddresses)

        // 선택한 교육 장소의 수강인원이 가득차 있는지
        selectedEducationAddresses.forEach {
            it.validateMaxParticipantExceeded(educationRepository.countByEducationAddressId(it.id!!))
        }

        // 교육 신청한 직원은 신청 완료에 대한 알림을 받을 수 있다.
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

        userRepository.update(user)

        return user
    }

    fun delete(id: Long) {
        val user = userRepository.findByIdOrThrow(id)
        userRepository.deleteById(user.id!!)
    }
}
