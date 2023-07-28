package com.meloning.megaCoffee.core.domain.store.model

import java.time.Instant

data class StoreEducationRelation(
    val id: Long? = null,
    val store: Store,
    val educationId: Long,
    var createdAt: Instant? = null
)
