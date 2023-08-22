package com.meloning.megaCoffee.core.domain.relation.model

import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import java.time.Instant

data class UserEducationPlaceRelation(
    val id: Long? = null,
    val userId: Long,
    val educationPlace: EducationPlace,
    var createdAt: Instant? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEducationPlaceRelation

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
