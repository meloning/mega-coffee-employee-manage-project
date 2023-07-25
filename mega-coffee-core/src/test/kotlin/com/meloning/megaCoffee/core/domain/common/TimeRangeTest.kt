package com.meloning.megaCoffee.core.domain.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalTime

class TimeRangeTest {
    @Test
    @DisplayName("타임 범위 생성 테스트")
    fun createTest() {
        // given
        val start = LocalTime.of(10, 0)
        val end = LocalTime.of(21, 0)

        // when
        val timeRange = TimeRange(start, end)

        // then
        assertThat(timeRange.startTime).isEqualTo(start)
        assertThat(timeRange.endTime).isEqualTo(end)
    }

    @Test
    @DisplayName("타임 범위 생성 실패 테스트")
    fun createFailTest() {
        // given
        val start = LocalTime.of(23, 0)
        val end = LocalTime.of(13, 0)

        // when, then
        Assertions.assertThatThrownBy {
            TimeRange(start, end)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("종료 시간은 시작 시간보다 커야합니다.")
    }
}
