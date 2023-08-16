package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationAddressRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IUserEducationAddressRelationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.UserEducationAddressRelationEntity
import org.springframework.stereotype.Repository

@Repository
class UserEducationAddressRelationRepositoryImpl(
    private val userEducationAddressRelationJpaRepository: UserEducationAddressRelationJpaRepository
) : IUserEducationAddressRelationRepository {

    override fun save(model: UserEducationAddressRelation): UserEducationAddressRelation {
        return userEducationAddressRelationJpaRepository.save(UserEducationAddressRelationEntity.from(model)).toModel()
    }

    override fun saveAll(models: List<UserEducationAddressRelation>): List<UserEducationAddressRelation> {
        return userEducationAddressRelationJpaRepository.saveAll(models.map { UserEducationAddressRelationEntity.from(it) }).map { it.toModel() }
    }
}
