package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.store.QStoreEducationRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.QUserEducationAddressRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.QUserEntity
import com.querydsl.jpa.impl.JPAQueryFactory

class CustomEducationJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomEducationJpaRepository {
    private val qUserEntity = QUserEntity.userEntity
    private val qEducationEntity = QEducationEntity.educationEntity
    private val qEducationAddressEntity = QEducationAddressEntity.educationAddressEntity
    private val qStoreEducationRelationEntity = QStoreEducationRelationEntity.storeEducationRelationEntity
    private val qUserEducationAddressRelationEntity = QUserEducationAddressRelationEntity.userEducationAddressRelationEntity

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<EducationEntity> {
        return jpaQueryFactory.selectFrom(qEducationEntity)
            .join(qStoreEducationRelationEntity)
            .on(
                qEducationEntity.id.eq(qStoreEducationRelationEntity.educationId)
                    .and(qStoreEducationRelationEntity.storeId.eq(storeId))
            )
            .leftJoin(qEducationEntity.educationAddresses.value, qEducationAddressEntity)
            .join(qUserEducationAddressRelationEntity)
            .on(
                qUserEntity.id.eq(qUserEducationAddressRelationEntity.userId)
                    .and(qUserEducationAddressRelationEntity.userId.eq(userId))
            )
            .fetch()
    }

    override fun findAllByStoreId(storeId: Long): List<EducationEntity> {
        return jpaQueryFactory.selectFrom(qEducationEntity)
            .join(qStoreEducationRelationEntity)
            .on(
                qEducationEntity.id.eq(qStoreEducationRelationEntity.educationId)
                    .and(qStoreEducationRelationEntity.storeId.eq(storeId))
            )
            .fetch()
    }
}
