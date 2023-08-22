package com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity

import com.meloning.megaCoffee.core.domain.relation.model.UserEducationPlaceRelation
import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationPlaceEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "user_education_place_relation")
class UserEducationPlaceRelationEntity : CreatedAtEntity {

    constructor(id: Long?, userId: Long, educationPlace: EducationPlaceEntity) : super() {
        this.id = id
        this.userId = userId
        this.educationPlace = educationPlace
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val userId: Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_place_id", nullable = false)
    val educationPlace: EducationPlaceEntity

    fun toModel() = UserEducationPlaceRelation(
        id = id!!,
        userId = userId,
        educationPlace = educationPlace.toModel(),
        createdAt = createdAt
    )

    companion object {
        @JvmStatic
        fun from(model: UserEducationPlaceRelation) = with(model) {
            UserEducationPlaceRelationEntity(
                id = id,
                userId = userId,
                educationPlace = EducationPlaceEntity.from(educationPlace)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEducationPlaceRelationEntity

        if (userId != other.userId) return false
        if (educationPlace != other.educationPlace) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + educationPlace.hashCode()
        return result
    }
}
