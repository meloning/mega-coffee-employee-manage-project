package com.meloning.megaCoffee.core.domain.relation.repository

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationAddressRelation

interface IUserEducationAddressRelationRepository {
    fun save(model: UserEducationAddressRelation): UserEducationAddressRelation
    fun saveAll(models: List<UserEducationAddressRelation>): List<UserEducationAddressRelation>
}
