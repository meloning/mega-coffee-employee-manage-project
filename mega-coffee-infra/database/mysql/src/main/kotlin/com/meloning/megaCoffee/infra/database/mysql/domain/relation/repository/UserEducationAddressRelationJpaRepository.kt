package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.UserEducationAddressRelationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEducationAddressRelationJpaRepository : JpaRepository<UserEducationAddressRelationEntity, Long>
