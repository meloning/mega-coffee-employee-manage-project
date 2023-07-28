package com.meloning.megaCoffee.core.domain.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

    @ParameterizedTest
    @DisplayName("타임 범위와 겹치는지 테스트")
    @CsvSource(
        "13:00, 20:00, 14:00, 19:00, TRUE",
        "15:00, 20:00, 12:00, 14:00, FALSE",
        "13:30, 20:50, 10:00, 13:30, TRUE",
        "13:00, 20:00, 20:00, 21:00, TRUE",
    )
    fun overlapsTest(start: String, end: String, otherStart: String, otherEnd: String, expectedResult: Boolean) {
        // given
        val timeRange = TimeRange(LocalTime.parse(start), LocalTime.parse(end))
        val other = TimeRange(LocalTime.parse(otherStart), LocalTime.parse(otherEnd))

        // when
        val result = timeRange.overlapsWith(other)

        // then
        assertThat(result).isEqualTo(expectedResult)
    }
}
