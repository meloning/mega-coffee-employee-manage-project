package com.meloning.megaCoffee.core.domain.common

import java.time.LocalTime

data class TimeRange(
    var startTime: LocalTime,
    var endTime: LocalTime
) {
    init {
        require(startTime < endTime) { "종료 시간은 시작 시간보다 커야합니다." }
    }
}
