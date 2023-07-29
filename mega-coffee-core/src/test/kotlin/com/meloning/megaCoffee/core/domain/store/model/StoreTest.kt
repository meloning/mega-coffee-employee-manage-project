package com.meloning.megaCoffee.core.domain.store.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.exception.NotRegisterException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalTime

class StoreTest {

    @Test
    @DisplayName("매장이 들어야할 교육 프로그램이 맞는지 체크하는 테스트")
    fun validateEducationTest() {
        // given
        val testStore = Store(
            id = 1,
            name = Name("관악점"),
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = Address("서울", "관악구", "신림동"),
            timeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(22, 0)),
        ).apply {
            addEducation(1)
            addEducation(2)
            addEducation(3)
        }

        // when, then
        Assertions.assertThatThrownBy {
            testStore.validateEligibility(4, "테스트")
        }.isInstanceOf(NotRegisterException::class.java)
            .hasMessage("${testStore.name.value} 매장의 직원은 테스트 교육 프로그램을 들을 수 없습니다.")
    }

    @Test
    @DisplayName("매장이 등록한 교육 프로그램을 삭제하는 테스트")
    fun removeEducationTest() {
        // given
        val testStore = Store(
            id = 1,
            name = Name("관악점"),
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = Address("서울", "관악구", "신림동"),
            timeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(22, 0)),
        ).apply {
            addEducation(1)
            addEducation(2)
            addEducation(3)
        }

        // when
        testStore.removeEducation(2)

        // then
        assertThat(testStore.educations.size).isEqualTo(2)
        assertThat(testStore.educations.map { it.educationId }).containsExactly(1, 3)
    }
}
