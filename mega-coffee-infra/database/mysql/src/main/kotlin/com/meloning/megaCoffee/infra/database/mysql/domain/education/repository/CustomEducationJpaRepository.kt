package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationPlaceEntity
import java.time.LocalDate

interface CustomEducationJpaRepository {
    fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationPlaceEntity>>
    fun findAllByStoreId(storeId: Long): List<EducationEntity>
    fun findEducationPlaceAllByUserId(userId: Long): List<EducationPlaceEntity>
    fun findEducationPlaceAllByDate(date: LocalDate): List<EducationPlaceEntity>
}
