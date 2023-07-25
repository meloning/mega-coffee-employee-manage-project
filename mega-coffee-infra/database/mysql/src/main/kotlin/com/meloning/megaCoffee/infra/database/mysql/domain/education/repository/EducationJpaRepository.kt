package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EducationJpaRepository : JpaRepository<EducationEntity, Long> {
    fun existsByName(name: NameVO): Boolean
}
