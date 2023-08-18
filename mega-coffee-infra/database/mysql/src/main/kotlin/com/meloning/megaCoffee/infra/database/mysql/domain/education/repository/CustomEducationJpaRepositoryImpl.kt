package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationPlaceEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationPlaceEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.QStoreEducationRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.QUserEducationPlaceRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.QStoreEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.QUserEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate

class CustomEducationJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomEducationJpaRepository {
    private val qUserEntity = QUserEntity.userEntity
    private val qStoreEntity = QStoreEntity.storeEntity
    private val qEducationEntity = QEducationEntity.educationEntity
    private val qEducationPlaceEntity = QEducationPlaceEntity.educationPlaceEntity
    private val qStoreEducationRelationEntity = QStoreEducationRelationEntity.storeEducationRelationEntity
    private val qUserEducationPlaceRelationEntity = QUserEducationPlaceRelationEntity.userEducationPlaceRelationEntity

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationPlaceEntity>> {
        val educations = jpaQueryFactory.select(qEducationEntity)
            .from(qStoreEducationRelationEntity)
            .innerJoin(qStoreEducationRelationEntity.education, qEducationEntity)
            .join(qStoreEntity)
            .on(
                qStoreEducationRelationEntity.storeId.eq(qStoreEntity.id)
                    .and(qStoreEducationRelationEntity.storeId.eq(storeId)).and(qStoreEntity.deleted.isFalse)
            )
            .fetch()

        val userEducationPlaces = jpaQueryFactory.select(qEducationPlaceEntity)
            .from(qUserEducationPlaceRelationEntity)
            .innerJoin(qUserEducationPlaceRelationEntity.educationPlace, qEducationPlaceEntity)
            .join(qUserEntity)
            .on(
                qUserEducationPlaceRelationEntity.userId.eq(qUserEntity.id)
                    .and(qUserEducationPlaceRelationEntity.userId.eq(userId)).and(qUserEntity.deleted.isFalse)
            )
            .fetch()

        return educations to userEducationPlaces
    }

    override fun findAllByStoreId(storeId: Long): List<EducationEntity> {
        return jpaQueryFactory.select(qEducationEntity)
            .from(qStoreEducationRelationEntity)
            .innerJoin(qStoreEducationRelationEntity.education, qEducationEntity)
            .where(
                qStoreEducationRelationEntity.storeId.eq(storeId)
            )
            .fetch()
    }

    override fun findEducationPlaceAllByUserId(userId: Long): List<EducationPlaceEntity> {
        return jpaQueryFactory.select(qEducationPlaceEntity)
            .from(qUserEducationPlaceRelationEntity)
            .innerJoin(qUserEducationPlaceRelationEntity.educationPlace, qEducationPlaceEntity)
            .where(
                qUserEducationPlaceRelationEntity.userId.eq(userId)
            )
            .fetch()
    }

    override fun findEducationPlaceAllByDate(date: LocalDate): List<EducationPlaceEntity> {
        return jpaQueryFactory.selectFrom(qEducationPlaceEntity)
            .where(qEducationPlaceEntity.date.eq(date))
            .fetch()
    }
}
