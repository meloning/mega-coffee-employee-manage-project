package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.QStoreEducationRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.QUserEducationAddressRelationEntity
import com.querydsl.jpa.impl.JPAQueryFactory

class CustomEducationJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomEducationJpaRepository {
    private val qEducationEntity = QEducationEntity.educationEntity
    private val qEducationAddressEntity = QEducationAddressEntity.educationAddressEntity
    private val qStoreEducationRelationEntity = QStoreEducationRelationEntity.storeEducationRelationEntity
    private val qUserEducationAddressRelationEntity = QUserEducationAddressRelationEntity.userEducationAddressRelationEntity

    override fun countByEducationAddressId(educationAddressId: Long): Int {
        return jpaQueryFactory.selectFrom(qUserEducationAddressRelationEntity)
            .where(qUserEducationAddressRelationEntity.educationAddressId.eq(educationAddressId))
            .fetch()
            .size
    }

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationAddressEntity>> {
        val educations = jpaQueryFactory.selectFrom(qEducationEntity)
            .join(qStoreEducationRelationEntity)
            .on(
                qEducationEntity.id.eq(qStoreEducationRelationEntity.educationId)
                    .and(qStoreEducationRelationEntity.store.id.eq(storeId))
            )
            .leftJoin(qEducationEntity.educationAddresses.value, qEducationAddressEntity)
            .join(qUserEducationAddressRelationEntity)
            .on(
                qEducationAddressEntity.id.eq(qUserEducationAddressRelationEntity.educationAddressId)
            )
            .where(qUserEducationAddressRelationEntity.user.id.eq(userId))
            .fetch()

        val userEducationAddresses = jpaQueryFactory.selectFrom(qEducationAddressEntity)
            .join(qUserEducationAddressRelationEntity)
            .on(qEducationAddressEntity.id.eq(qUserEducationAddressRelationEntity.educationAddressId))
            .where(qUserEducationAddressRelationEntity.user.id.eq(userId))
            .fetch()

        return educations to userEducationAddresses
    }

    override fun findAllByStoreId(storeId: Long): List<EducationEntity> {
        return jpaQueryFactory.selectFrom(qEducationEntity)
            .join(qStoreEducationRelationEntity)
            .on(
                qEducationEntity.id.eq(qStoreEducationRelationEntity.educationId)
                    .and(qStoreEducationRelationEntity.store.id.eq(storeId))
            )
            .fetch()
    }
}
