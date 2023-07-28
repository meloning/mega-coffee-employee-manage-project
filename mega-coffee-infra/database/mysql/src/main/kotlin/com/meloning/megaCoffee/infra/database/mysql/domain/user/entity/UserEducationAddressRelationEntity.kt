package com.meloning.megaCoffee.infra.database.mysql.domain.user.entity

import com.meloning.megaCoffee.core.domain.user.model.UserEducationAddressRelation
import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import org.hibernate.proxy.HibernateProxy
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "user_education_address_relation")
class UserEducationAddressRelationEntity : CreatedAtEntity {

    constructor(id: Long?, user: UserEntity, educationAddressId: Long) : super() {
        this.id = id
        this.user = user
        this.educationAddressId = educationAddressId
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity

    val educationAddressId: Long

    fun toModel() = UserEducationAddressRelation(
        id = id!!,
        user = user.toModel(),
        educationAddressId = educationAddressId,
        createdAt = createdAt
    )

    companion object {
        @JvmStatic
        fun from(model: UserEducationAddressRelation) = with(model) {
            UserEducationAddressRelationEntity(
                id = id,
                user = UserEntity.from(user),
                educationAddressId = educationAddressId
            )
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as UserEducationAddressRelationEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
