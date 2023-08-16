package com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationAddressRelation
import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationAddressEntity
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

    constructor(id: Long?, userId: Long, educationAddress: EducationAddressEntity) : super() {
        this.id = id
        this.userId = userId
        this.educationAddress = educationAddress
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val userId: Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_address_id", nullable = false)
    val educationAddress: EducationAddressEntity

    fun toModel() = UserEducationAddressRelation(
        id = id!!,
        userId = userId,
        educationAddress = educationAddress.toModel(),
        createdAt = createdAt
    )

    companion object {
        @JvmStatic
        fun from(model: UserEducationAddressRelation) = with(model) {
            UserEducationAddressRelationEntity(
                id = id,
                userId = userId,
                educationAddress = EducationAddressEntity.from(educationAddress)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEducationAddressRelationEntity

        if (userId != other.userId) return false
        if (educationAddress != other.educationAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + educationAddress.hashCode()
        return result
    }
}
