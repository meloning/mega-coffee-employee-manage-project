package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationAddressEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.QEducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.QStoreEducationRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.QUserEducationAddressRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.QStoreEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.QUserEntity
import com.querydsl.jpa.impl.JPAQueryFactory

class CustomEducationJpaRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomEducationJpaRepository {
    private val qUserEntity = QUserEntity.userEntity
    private val qStoreEntity = QStoreEntity.storeEntity
    private val qEducationEntity = QEducationEntity.educationEntity
    private val qEducationAddressEntity = QEducationAddressEntity.educationAddressEntity
    private val qStoreEducationRelationEntity = QStoreEducationRelationEntity.storeEducationRelationEntity
    private val qUserEducationAddressRelationEntity = QUserEducationAddressRelationEntity.userEducationAddressRelationEntity

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): Pair<List<EducationEntity>, List<EducationAddressEntity>> {
        val educations = jpaQueryFactory.select(qEducationEntity)
            .from(qStoreEducationRelationEntity)
            .innerJoin(qStoreEducationRelationEntity.education, qEducationEntity)
            .join(qStoreEntity)
            .on(
                qStoreEducationRelationEntity.storeId.eq(qStoreEntity.id)
                    .and(qStoreEducationRelationEntity.storeId.eq(storeId)).and(qStoreEntity.deleted.isFalse)
            )
            .fetch()

        val userEducationAddresses = jpaQueryFactory.select(qEducationAddressEntity)
            .from(qUserEducationAddressRelationEntity)
            .innerJoin(qUserEducationAddressRelationEntity.educationAddress, qEducationAddressEntity)
            .join(qUserEntity)
            .on(
                qUserEducationAddressRelationEntity.userId.eq(qUserEntity.id)
                    .and(qUserEducationAddressRelationEntity.userId.eq(userId)).and(qUserEntity.deleted.isFalse)
            )
            .fetch()

        return educations to userEducationAddresses
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

    override fun findEducationAddressAllByUserId(userId: Long): List<EducationAddressEntity> {
        return jpaQueryFactory.select(qEducationAddressEntity)
            .from(qUserEducationAddressRelationEntity)
            .innerJoin(qUserEducationAddressRelationEntity.educationAddress, qEducationAddressEntity)
            .where(
                qUserEducationAddressRelationEntity.userId.eq(userId)
            )
            .fetch()
    }
}
