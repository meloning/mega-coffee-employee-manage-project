package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.exception.AlreadyFullException
import com.meloning.megaCoffee.core.exception.ConflictFieldException
import com.meloning.megaCoffee.core.exception.NotFoundException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class EducationAddressesTest {

    @Test
    @DisplayName("교육 장소들 생성 테스트")
    fun createTest() {
        // given
        val mockEducation = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddressList = mutableListOf(
            EducationAddress(1, mockEducation, Address("서울", "관악구", "123"), 13, LocalDate.of(2023, 1, 27), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(2, mockEducation, Address("서울", "강서구", "123"), 15, LocalDate.of(2023, 4, 19), TimeRange(LocalTime.of(13, 0), LocalTime.of(17, 0))),
            EducationAddress(3, mockEducation, Address("서울", "강남구", "123"), 4, LocalDate.now(), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(4, mockEducation, Address("서울", "구로구", "123"), 9, LocalDate.now(), TimeRange(LocalTime.of(12, 0), LocalTime.of(18, 0))),
            EducationAddress(5, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(1, 0), LocalTime.of(23, 0))),
        )

        // when
        val educationAddresses = EducationAddresses(educationAddressList)

        // then
        assertThat(educationAddresses.size()).isEqualTo(5)
    }

    @Test
    @DisplayName("등록된 교육 장소들이 같은날, 시간, 장소가 겹치는 경우 테스트")
    fun validateDuplicatePlaceTimeSlotsTest() {
        // given
        val mockEducation = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddressList = mutableListOf(
            EducationAddress(1, mockEducation, Address("서울", "관악구", "123"), 13, LocalDate.of(2023, 1, 27), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(2, mockEducation, Address("서울", "강서구", "123"), 15, LocalDate.of(2023, 4, 19), TimeRange(LocalTime.of(13, 0), LocalTime.of(17, 0))),
            EducationAddress(3, mockEducation, Address("서울", "강남구", "123"), 4, LocalDate.now(), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(4, mockEducation, Address("서울", "강남구", "123"), 9, LocalDate.now(), TimeRange(LocalTime.of(9, 30), LocalTime.of(18, 0))),
            EducationAddress(5, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(1, 0), LocalTime.of(23, 0))),
        )

        // when, then
        Assertions.assertThatThrownBy {
            EducationAddresses(educationAddressList)
        }.isInstanceOf(ConflictFieldException::class.java)
            .hasMessage("장소, 날짜, 시간대가 겹쳐 등록할 수 없습니다.")
    }

    @Test
    @DisplayName("등록된 교육장소들의 크기가 최대치 이상인 경우")
    fun validateMaxPlaceExceededTest() {
        // given
        val mockEducation = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddressList = mutableListOf(
            EducationAddress(1, mockEducation, Address("서울", "관악구", "123"), 13, LocalDate.of(2023, 1, 27), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(2, mockEducation, Address("서울", "강서구", "123"), 15, LocalDate.of(2023, 4, 19), TimeRange(LocalTime.of(13, 0), LocalTime.of(17, 0))),
            EducationAddress(3, mockEducation, Address("서울", "강남구", "123"), 4, LocalDate.now(), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(4, mockEducation, Address("서울", "구로구", "123"), 9, LocalDate.now(), TimeRange(LocalTime.of(12, 0), LocalTime.of(18, 0))),
            EducationAddress(5, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(1, 0), LocalTime.of(23, 0))),
            EducationAddress(6, mockEducation, Address("서울", "서초구", "123"), 21, LocalDate.of(2023, 12, 25), TimeRange(LocalTime.of(9, 0), LocalTime.of(23, 0))),
        )

        // when, then
        Assertions.assertThatThrownBy {
            EducationAddresses(educationAddressList)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("교육 장소는 최대 5개까지 등록할 수 있습니다.")
    }

    @Test
    @DisplayName("입력받은 교육 장소들이 존재하는지 테스트")
    fun validateExistingTest() {
        // given
        val mockEducation = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddressList = mutableListOf(
            EducationAddress(1, mockEducation, Address("서울", "관악구", "123"), 13, LocalDate.of(2023, 1, 27), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(2, mockEducation, Address("서울", "강서구", "123"), 15, LocalDate.of(2023, 4, 19), TimeRange(LocalTime.of(13, 0), LocalTime.of(17, 0))),
            EducationAddress(3, mockEducation, Address("서울", "강남구", "123"), 4, LocalDate.now(), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(4, mockEducation, Address("서울", "구로구", "123"), 9, LocalDate.now(), TimeRange(LocalTime.of(12, 0), LocalTime.of(18, 0))),
            EducationAddress(5, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(1, 0), LocalTime.of(23, 0))),
        )

        val educationAddresses = EducationAddresses(educationAddressList)

        // when, then
        Assertions.assertThatThrownBy {
            educationAddresses.validateExisting(listOf(1, 3, 5, 7))
        }.isInstanceOf(NotFoundException::class.java)
            .hasMessage("존재하지 않는 교육 장소들이 있습니다.")
    }

    @Test
    @DisplayName("교육 프로그램의 장소 추가시 오류 테스트")
    fun addEducationAddressFailedTest() {
        // given
        val mockEducation = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        val educationAddressList = mutableListOf(
            EducationAddress(1, mockEducation, Address("서울", "관악구", "123"), 13, LocalDate.of(2023, 1, 27), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(2, mockEducation, Address("서울", "강서구", "123"), 15, LocalDate.of(2023, 4, 19), TimeRange(LocalTime.of(13, 0), LocalTime.of(17, 0))),
            EducationAddress(3, mockEducation, Address("서울", "강남구", "123"), 4, LocalDate.now(), TimeRange(LocalTime.of(8, 0), LocalTime.of(11, 0))),
            EducationAddress(4, mockEducation, Address("서울", "구로구", "123"), 9, LocalDate.now(), TimeRange(LocalTime.of(12, 0), LocalTime.of(18, 0))),
            EducationAddress(5, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(10, 0), LocalTime.of(23, 0))),
        )

        val mockEducationAddress = EducationAddress(6, mockEducation, Address("서울", "송파구", "123"), 21, LocalDate.of(2023, 12, 24), TimeRange(LocalTime.of(1, 0), LocalTime.of(9, 0)))

        val educationAddresses = EducationAddresses(educationAddressList)

        // when, then
        Assertions.assertThatThrownBy {
            educationAddresses.add(mockEducationAddress)
        }.isInstanceOf(AlreadyFullException::class.java)
            .hasMessage("이미 교육장소들이 가득찼습니다.")
    }
}
