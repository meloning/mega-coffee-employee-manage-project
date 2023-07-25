package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.TimeRange
import java.time.Instant
import java.time.LocalDate

data class EducationAddress(
    val id: Long? = null,
    var education: Education,
    var address: Address,
    var maxParticipant: Int,
    var date: LocalDate,
    var timeRange: TimeRange,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
)
