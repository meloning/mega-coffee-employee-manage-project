package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class EducationAddressTest {

    @Test
    @DisplayName("같은날, 시간대가 겹치는지 테스트")
    fun sameDateTimeSlotsTest() {
        // given
        val education = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddress = EducationAddress(
            id = 1,
            education = education,
            address = Address("서울", "관악구", "123"),
            maxParticipant = 23,
            date = LocalDate.now(),
            timeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(20, 0))
        )

        val target = EducationAddress(
            id = 1,
            education = education,
            address = Address("서울", "강서구", "123"),
            maxParticipant = 13,
            date = LocalDate.now(),
            timeRange = TimeRange(LocalTime.of(15, 0), LocalTime.of(22, 0))
        )

        // when
        val result = educationAddress.isSameDateTimeSlots(target)

        // then
        assertThat(result).isTrue()
    }

    @Test
    @DisplayName("같은날, 같은 장소, 시간대가 겹치는지 검증하는 테스트")
    fun sameDatePlaceTimeSlotsTest() {
        // given
        val education = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddress = EducationAddress(
            id = 1,
            education = education,
            address = Address("서울", "관악구", "123"),
            maxParticipant = 23,
            date = LocalDate.now(),
            timeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(20, 0))
        )

        val target = EducationAddress(
            id = 1,
            education = education,
            address = Address("서울", "관악구", "123"),
            maxParticipant = 13,
            date = LocalDate.now(),
            timeRange = TimeRange(LocalTime.of(15, 0), LocalTime.of(22, 0))
        )

        // when
        val result = educationAddress.isSameDatePlaceTimeSlots(target)

        // then
        assertThat(result).isTrue()
    }

    @Test
    @DisplayName("해당 장소의 수강인원이 이미 가득찬 경우 테스트")
    fun validateMaxParticipantExceededTest() {
        // given
        val education = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddress = EducationAddress(
            id = 1,
            education = education,
            address = Address("서울", "관악구", "123"),
            maxParticipant = 23,
            date = LocalDate.now(),
            timeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(20, 0))
        )

        // when, then
        Assertions.assertThatThrownBy {
            educationAddress.validateMaxParticipantExceeded(23)
        }.isInstanceOf(RuntimeException::class.java)
            .hasMessage("선택한 교육장소(1)의 수강인원(23)이 가득찼습니다.")
    }
}
