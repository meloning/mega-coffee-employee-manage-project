package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IStoreEducationRelationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.StoreEducationRelationEntity
import org.springframework.stereotype.Repository

@Repository
class StoreEducationRelationRepositoryImpl(
    private val storeEducationRelationJpaRepository: StoreEducationRelationJpaRepository
) : IStoreEducationRelationRepository {
    override fun save(model: StoreEducationRelation): StoreEducationRelation {
        return storeEducationRelationJpaRepository.save(StoreEducationRelationEntity.from(model)).toModel()
    }

    override fun saveAll(models: List<StoreEducationRelation>): List<StoreEducationRelation> {
        return storeEducationRelationJpaRepository.saveAll(models.map { StoreEducationRelationEntity.from(it) }).map { it.toModel() }
    }
}
