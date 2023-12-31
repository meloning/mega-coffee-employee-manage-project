package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.exception.AlreadyFullException
import java.time.Instant
import java.time.LocalDate

data class EducationPlace(
    val id: Long? = null,
    var education: Education,
    var address: Address,
    var maxParticipant: Int,
    var currentParticipant: Int = 0,
    var date: LocalDate,
    var timeRange: TimeRange,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {

    fun isSameDateTimeSlots(other: EducationPlace): Boolean {
        return this.timeRange.overlapsWith(other.timeRange) && this.date == date
    }

    fun isSameDatePlaceTimeSlots(other: EducationPlace): Boolean {
        return this.address == other.address &&
            isSameDateTimeSlots(other)
    }

    private fun isParticipantExceeded(): Boolean {
        return currentParticipant == maxParticipant
    }

    fun validateMaxParticipantExceeded() {
        if (isParticipantExceeded()) {
            throw AlreadyFullException("선택한 교육장소($id)의 수강인원($maxParticipant)이 가득찼습니다.")
        }
    }

    fun increaseCurrentParticipant() {
        this.currentParticipant++
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EducationPlace

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
