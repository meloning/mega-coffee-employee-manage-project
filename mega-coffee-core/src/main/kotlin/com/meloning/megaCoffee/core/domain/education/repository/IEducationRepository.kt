package com.meloning.megaCoffee.core.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education

interface IEducationRepository {
    fun save(education: Education): Education
    fun saveAll(educations: List<Education>): List<Education>
    fun update(education: Education)

    fun findById(id: Long): Education?
    fun findDetailById(id: Long): Education?
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<Education>
    fun findAllByStoreId(storeId: Long): List<Education>
    fun countByEducationAddressId(educationAddressId: Long): Int

    fun existsByName(name: Name): Boolean

    fun deleteById(id: Long)
}

fun IEducationRepository.findDetailByIdOrThrow(id: Long): Education =
    this.findDetailById(id) ?: throw RuntimeException("교육프로그램이 존재하지 않습니다.")

fun IEducationRepository.findByIdOrThrow(id: Long): Education =
    this.findById(id) ?: throw RuntimeException("교육프로그램이 존재하지 않습니다.")
