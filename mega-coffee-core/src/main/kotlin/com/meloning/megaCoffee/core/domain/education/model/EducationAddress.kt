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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EducationAddress

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
