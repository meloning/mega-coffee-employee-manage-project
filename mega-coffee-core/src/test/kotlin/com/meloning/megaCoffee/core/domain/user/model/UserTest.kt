package com.meloning.megaCoffee.core.domain.user.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    @DisplayName("유저가 교육 신청시 이미 등록된 장소인지 검증하는 테스트")
    fun validateExistEducationAddressTest() {
        // given
        val testUser = User(
            id = 1,
            email = "melon8372@gmail.com",
            name = Name("메로닝"),
            homeAddress = Address("서울", "관악", "1234"),
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber("01012341234"),
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 1,
        ).apply {
            addEducationAddress(1)
            addEducationAddress(2)
            addEducationAddress(3)
            addEducationAddress(4)
        }

        // when, then
        Assertions.assertThatThrownBy {
            testUser.validateExistingEducationAddress(listOf(1, 3, 4))
        }.isInstanceOf(RuntimeException::class.java)
            .hasMessage("이미 등록한 교육장소를 신청하였습니다. 재등록 바랍니다.")
    }

    @Test
    @DisplayName("유저가 신청한 교육 장소르 삭제하는 테스트")
    fun removeUserEducationAddressTest() {
        // given
        val testUser = User(
            id = 1,
            email = "melon8372@gmail.com",
            name = Name("메로닝"),
            homeAddress = Address("서울", "관악", "1234"),
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber("01012341234"),
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 1,
        ).apply {
            addEducationAddress(1)
            addEducationAddress(2)
            addEducationAddress(3)
            addEducationAddress(4)
        }

        // when
        testUser.removeEducationAddress(1)
        testUser.removeEducationAddress(4)

        assertThat(testUser.educationAddressRelations.size).isEqualTo(2)
        assertThat(testUser.educationAddressRelations.map { it.educationAddressId }).containsExactly(2, 3)
    }
}
