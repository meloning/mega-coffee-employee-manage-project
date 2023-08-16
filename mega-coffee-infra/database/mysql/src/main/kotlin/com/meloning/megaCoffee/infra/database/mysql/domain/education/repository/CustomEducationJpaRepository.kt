package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity

interface CustomEducationJpaRepository {
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationAddressEntity>>
    fun findAllByStoreId(storeId: Long): List<EducationEntity>
    fun findEducationAddressAllByUserId(userId: Long): List<EducationAddressEntity>
}
