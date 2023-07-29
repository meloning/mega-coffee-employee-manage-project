package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.common.TimeRange
import java.time.LocalTime

data class TimeRangeRequest(
    val startTime: String,
    val endTime: String
) {

    fun toModel() = TimeRange(LocalTime.parse(startTime), LocalTime.parse(endTime))
}
