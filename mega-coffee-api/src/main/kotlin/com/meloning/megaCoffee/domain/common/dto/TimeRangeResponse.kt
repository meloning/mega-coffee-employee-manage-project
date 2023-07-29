package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.common.TimeRange

data class TimeRangeResponse(
    val startTime: String,
    val endTime: String
) {

    companion object {
        @JvmStatic
        fun from(model: TimeRange) = with(model) {
            TimeRangeResponse(
                startTime = startTime.toString(),
                endTime = endTime.toString()
            )
        }
    }
}
