package com.meloning.megaCoffee.core.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.exception.NotFoundException
import java.time.LocalDate

interface IEducationRepository {
    fun save(education: Education): Education
    fun saveAll(educations: List<Education>): List<Education>
    fun update(education: Education)

    fun findById(id: Long): Education?
    fun findDetailById(id: Long): Education?
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<Education>
    fun findAllByStoreId(storeId: Long): List<Education>
    fun findEducationPlaceAllByUserId(userId: Long): List<EducationPlace>
    fun findEducationPlaceAllByDate(date: LocalDate): List<EducationPlace>
    fun findParticipantAllByEducationPlaceId(id: Long): List<User>

    fun existsByName(name: Name): Boolean

    fun deleteById(id: Long)
}

fun IEducationRepository.findDetailByIdOrThrow(id: Long): Education =
    this.findDetailById(id) ?: throw NotFoundException("교육프로그램이 존재하지 않습니다.")

fun IEducationRepository.findByIdOrThrow(id: Long): Education =
    this.findById(id) ?: throw NotFoundException("교육프로그램이 존재하지 않습니다.")
