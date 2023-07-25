package com.meloning.megaCoffee.infra.database.mysql.domain.common

import com.meloning.megaCoffee.core.domain.common.TimeRange
import java.time.LocalTime
import javax.persistence.Column

data class TimeRangeVO(
    @Column(nullable = false, columnDefinition = "TIME")
    var startTime: LocalTime,
    @Column(nullable = false, columnDefinition = "TIME")
    var endTime: LocalTime
) {
    init {
        require(startTime < endTime) { "종료 시간은 시작 시간보다 커야한다." }
    }

    fun toModel() = TimeRange(startTime, endTime)

    companion object {
        @JvmStatic
        fun from(model: TimeRange) = TimeRangeVO(model.startTime, model.endTime)
    }
}
