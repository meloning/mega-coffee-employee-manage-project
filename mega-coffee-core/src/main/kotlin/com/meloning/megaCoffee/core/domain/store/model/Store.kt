package com.meloning.megaCoffee.core.domain.store.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import java.time.Instant

data class Store(
    val id: Long? = null,
    val name: Name,
    var type: StoreType,
    var ownerId: Long,
    var address: Address,
    var timeRange: TimeRange,
    // 교육 프로그램 정보들
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
)
