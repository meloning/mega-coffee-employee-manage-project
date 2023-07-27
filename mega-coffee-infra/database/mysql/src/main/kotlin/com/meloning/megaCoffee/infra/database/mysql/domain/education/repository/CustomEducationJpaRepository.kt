package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity

interface CustomEducationJpaRepository {
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<EducationEntity>
    fun findAllByStoreId(storeId: Long): List<EducationEntity>
}
