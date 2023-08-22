package com.meloning.megaCoffee.core.domain.education.usecase

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.education.repository.findDetailByIdOrThrow
import com.meloning.megaCoffee.core.domain.education.usecase.command.CreateEducationCommand
import com.meloning.megaCoffee.core.domain.education.usecase.command.RegisterEducationPlacesCommand
import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation
import com.meloning.megaCoffee.core.domain.relation.model.UserEducationPlaceRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IStoreEducationRelationRepository
import com.meloning.megaCoffee.core.domain.relation.repository.IUserEducationPlaceRelationRepository
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findNotDeletedByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.usecase.UserEducationPlaceValidator
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import com.meloning.megaCoffee.core.infra.event.EventSender
import com.meloning.megaCoffee.core.infra.event.EventType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class EducationService(
    private val userRepository: IUserRepository,
    private val storeRepository: IStoreRepository,
    private val storeEducationRelationRepository: IStoreEducationRelationRepository,
    private val educationRepository: IEducationRepository,
    private val userEducationPlaceRelationRepository: IUserEducationPlaceRelationRepository,
    private val eventSender: EventSender
) {

    @Transactional(readOnly = true)
    fun detail(id: Long): Education {
        return educationRepository.findDetailByIdOrThrow(id)
    }

    fun create(command: CreateEducationCommand): Education {
        if (educationRepository.existsByName(command.name)) {
            throw AlreadyExistException("이미 존재하는 교육프로그램입니다.")
        }

        return educationRepository.save(command.toModel())
    }

    fun registerAddress(id: Long, command: RegisterEducationPlacesCommand) {
        val education = educationRepository.findByIdOrThrow(id)
        education.update(command.toModel(education))

        educationRepository.update(education)
    }

    fun registerStore(id: Long, storeIds: List<Long>) {
        val education = educationRepository.findByIdOrThrow(id)
        val stores = storeRepository.findAllById(storeIds)

        val storeEducationRelations = stores.map {
            StoreEducationRelation.create(it.id!!, education)
        }
        storeEducationRelationRepository.saveAll(storeEducationRelations)

        val targetUsersInStore = userRepository.findAllByStoreIdIn(storeIds)

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

    fun registerParticipant(id: Long, userId: Long, educationPlaceIds: List<Long>) {
        val currentUser = userRepository.findByIdOrThrow(userId)
        val store = storeRepository.findNotDeletedByIdOrThrow(currentUser.storeId)
        val requiredEducationsByStore = educationRepository.findAllByStoreId(currentUser.storeId)

        val education = educationRepository.findDetailByIdOrThrow(id)
        val educationPlaces = education.educationPlaces

        // [Validate]
        education.run {
            validateUserEligibility(currentUser.employeeType)
            validateStoreEligibility(requiredEducationsByStore.map { it.id!! }, store.name.value)
        }

        // 새로 등록할 것들중 비교
        educationPlaces.run {
            validateExisting(educationPlaceIds)
            validateExpired(educationPlaceIds)
        }

        val selectedEducationPlaces = educationPlaces.filterByContainedIds(educationPlaceIds)
        val userEducationPlaces = educationRepository.findEducationPlaceAllByUserId(currentUser.id!!)

        UserEducationPlaceValidator.run {
            // 기존 등록한 것들과 비교
            validateAlreadyRegister(userEducationPlaces, selectedEducationPlaces.map { it.id!! })

            // 같은날, 겹치는 시간대가 있는지 검증
            validateDuplicateTimeSlots(selectedEducationPlaces + userEducationPlaces)
        }

        // 선택한 교육 장소의 수강인원이 가득차 있는지
        selectedEducationPlaces.forEach {
            it.validateMaxParticipantExceeded()
        }

        // 각 교육장소의 현재 참여자인원 증가
        selectedEducationPlaces.forEach {
            educationPlaces.findAndIncreaseCurrentParticipant(it.id!!)
        }

        // 유저가 신청한 교육장소들 등록 및 선택된 교육장소의 현재 참여자 인원수 증가
        val newUserEducationPlaceRelations = selectedEducationPlaces.map {
            UserEducationPlaceRelation(userId = currentUser.id, educationPlace = it)
        }
        userEducationPlaceRelationRepository.saveAll(newUserEducationPlaceRelations)
        educationRepository.update(education)

        // 교육 신청한 직원은 신청 완료에 대한 알림을 받을 수 있다.
        selectedEducationPlaces.forEach {
            eventSender.send(
                type = EventType.EMAIL,
                payload = mapOf(
                    "email" to currentUser.email,
                    "username" to currentUser.name.value,
                    "educationName" to education.name.value,
                    "educationPlaceAddress" to it.address.getAddress(),
                    "date" to it.date.toString(),
                    "time" to it.timeRange.toString(),
                    "type" to "complete_user_education"
                )
            )
        }
    }

    @Transactional(readOnly = true)
    fun getEducationPlace(date: LocalDate): List<EducationPlace> {
        return educationRepository.findEducationPlaceAllByDate(date)
    }

    @Transactional(readOnly = true)
    fun getParticipantsByPlaceId(educationPlaceId: Long): List<User> {
        return educationRepository.findParticipantAllByEducationPlaceId(educationPlaceId)
    }
}
