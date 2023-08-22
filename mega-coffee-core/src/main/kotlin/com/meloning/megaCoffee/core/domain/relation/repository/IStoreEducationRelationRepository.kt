package com.meloning.megaCoffee.core.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation

interface IStoreEducationRelationRepository {
    fun save(model: StoreEducationRelation): StoreEducationRelation
    fun saveAll(models: List<StoreEducationRelation>): List<StoreEducationRelation>
}
