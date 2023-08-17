package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationPlaceEntity

interface CustomEducationJpaRepository {
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationPlaceEntity>>
    fun findAllByStoreId(storeId: Long): List<EducationEntity>
    fun findEducationPlaceAllByUserId(userId: Long): List<EducationPlaceEntity>
}
