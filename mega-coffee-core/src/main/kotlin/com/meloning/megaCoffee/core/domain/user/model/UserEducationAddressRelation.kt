package com.meloning.megaCoffee.core.domain.user.model

import java.time.Instant

data class UserEducationAddressRelation(
    val id: Long? = null,
    val user: User,
    val educationAddressId: Long,
    var createdAt: Instant? = null
)
