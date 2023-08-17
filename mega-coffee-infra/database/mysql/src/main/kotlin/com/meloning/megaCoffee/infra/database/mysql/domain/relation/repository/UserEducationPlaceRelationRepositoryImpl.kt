package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationPlaceRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IUserEducationPlaceRelationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.UserEducationPlaceRelationEntity
import org.springframework.stereotype.Repository

@Repository
class UserEducationPlaceRelationRepositoryImpl(
    private val userEducationPlaceRelationJpaRepository: UserEducationPlaceRelationJpaRepository
) : IUserEducationPlaceRelationRepository {

    override fun save(model: UserEducationPlaceRelation): UserEducationPlaceRelation {
        return userEducationPlaceRelationJpaRepository.save(UserEducationPlaceRelationEntity.from(model)).toModel()
    }

    override fun saveAll(models: List<UserEducationPlaceRelation>): List<UserEducationPlaceRelation> {
        return userEducationPlaceRelationJpaRepository.saveAll(models.map { UserEducationPlaceRelationEntity.from(it) }).map { it.toModel() }
    }
}
