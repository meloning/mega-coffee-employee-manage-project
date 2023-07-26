package com.meloning.megaCoffee.core.domain.user.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.user.model.User

interface IUserRepository {
    fun save(user: User): User
    fun saveAll(users: List<User>): List<User>
    fun update(user: User)

    fun findById(id: Long): User?

    fun existsByNameAndEmail(name: Name, email: String): Boolean

    fun deleteById(id: Long)
}

fun IUserRepository.findByIdOrThrow(id: Long): User =
    this.findById(id) ?: throw RuntimeException("유저가 존재하지 않습니다.")
