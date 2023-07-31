package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.UserEducationAddressRelationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.UserEntity
import org.hibernate.Hibernate
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : IUserRepository {
    override fun findAll(command: ScrollUserCommand, page: Int, size: Int): InfiniteScrollType<Pair<User, Store>> {
        val (content, hasNext) = userJpaRepository.scroll(command, page, size)
        return content.map { it.toModel() to it.store.toModel() } to hasNext
    }

    override fun save(user: User): User {
        return userJpaRepository.save(UserEntity.from(user)).toModel()
    }

    override fun saveAll(users: List<User>): List<User> {
        return userJpaRepository.saveAll(users.map { UserEntity.from(it) }).map { it.toModel() }
    }

    override fun update(user: User) {
        userJpaRepository.save(
            UserEntity.from(user).apply {
                update(user.educationAddressRelations.map { UserEducationAddressRelationEntity.from(it) }.toMutableList())
            }
        )
    }

    override fun findById(id: Long): User? {
        return userJpaRepository.findByIdAndDeletedIsFalse(id)?.toModel()
    }

    override fun existsByNameAndEmail(name: Name, email: String): Boolean {
        return userJpaRepository.existsByNameAndEmailAndDeletedIsFalse(NameVO.from(name), email)
    }

    override fun deleteById(id: Long) {
        return userJpaRepository.deleteById(id)
    }

    override fun findDetailById(id: Long): User? {
        val userEntity = userJpaRepository.findByIdAndDeletedIsFalse(id)
        userEntity?.educationAddressRelations?.forEach {
            Hibernate.initialize(it)
        }
        return userEntity?.toModel()?.apply {
            update(userEntity.educationAddressRelations.map { it.toModel() }.toMutableList())
        }
    }
}
