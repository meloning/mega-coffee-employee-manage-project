package com.meloning.megaCoffee.infra.database.mysql.domain.relation.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity.UserEducationPlaceRelationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEducationPlaceRelationJpaRepository : JpaRepository<UserEducationPlaceRelationEntity, Long>
