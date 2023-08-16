package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.StoreEducationRelationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StoreEducationRelationJpaRepository : JpaRepository<StoreEducationRelationEntity, Long>
