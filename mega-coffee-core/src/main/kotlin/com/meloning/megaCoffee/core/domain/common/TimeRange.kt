package com.meloning.megaCoffee.core.domain.common

import java.time.LocalTime

data class TimeRange(
    var startTime: LocalTime,
    var endTime: LocalTime
) {
    init {
        require(startTime < endTime) { "종료 시간은 시작 시간보다 커야합니다." }
    }

    fun overlapsWith(other: TimeRange): Boolean {
        return startTime <= other.endTime && other.startTime <= endTime
    }

    override fun toString(): String {
        return "$startTime ~ $endTime"
    }

    companion object {
        @JvmField
        val DUMMY = TimeRange(LocalTime.MIN, LocalTime.MAX)
    }
}
