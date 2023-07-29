package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.common.TimeRange
import java.time.LocalTime
import javax.validation.constraints.NotBlank

data class TimeRangeRequest(
    @field:NotBlank(message = "비어있거나 공백일 수 없습니다.")
    val startTime: String,
    @field:NotBlank(message = "비어있거나 공백일 수 없습니다.")
    val endTime: String
) {

    fun toModel() = TimeRange(LocalTime.parse(startTime), LocalTime.parse(endTime))
}
