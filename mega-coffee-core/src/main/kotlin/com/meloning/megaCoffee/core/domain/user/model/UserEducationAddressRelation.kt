package com.meloning.megaCoffee.core.domain.user.model

import java.time.Instant

data class UserEducationAddressRelation(
    val id: Long? = null,
    val user: User,
    val educationAddressId: Long,
    var createdAt: Instant? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEducationAddressRelation

        if (user != other.user) return false
        if (educationAddressId != other.educationAddressId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + educationAddressId.hashCode()
        return result
    }
}
