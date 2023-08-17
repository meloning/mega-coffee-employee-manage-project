package com.meloning.megaCoffee.core.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationPlaceRelation

interface IUserEducationPlaceRelationRepository {
    fun save(model: UserEducationPlaceRelation): UserEducationPlaceRelation
    fun saveAll(models: List<UserEducationPlaceRelation>): List<UserEducationPlaceRelation>
}
