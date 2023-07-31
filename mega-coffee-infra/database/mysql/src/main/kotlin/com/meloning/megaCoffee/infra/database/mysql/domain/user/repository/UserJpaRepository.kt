package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository

import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long>, CustomUserJpaRepository {
    fun existsByNameAndEmailAndDeletedIsFalse(name: NameVO, email: String): Boolean
    fun findByIdAndDeletedIsFalse(id: Long): UserEntity?
}
