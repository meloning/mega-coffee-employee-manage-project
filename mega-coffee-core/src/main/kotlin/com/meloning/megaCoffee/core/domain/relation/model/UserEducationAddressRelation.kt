package com.meloning.megaCoffee.core.domain.relation.model

import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import java.time.Instant

data class UserEducationAddressRelation(
    val id: Long? = null,
    val userId: Long,
    val educationAddress: EducationAddress,
    var createdAt: Instant? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEducationAddressRelation

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
