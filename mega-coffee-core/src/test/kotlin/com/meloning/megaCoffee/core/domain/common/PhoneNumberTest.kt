package com.meloning.megaCoffee.core.domain.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PhoneNumberTest {

    @Test
    @DisplayName("휴대폰 번호 생성 테스트")
    fun createTest() {
        // given
        val phone = "01012345678"

        // when
        val phoneNumber = PhoneNumber(phone)

        // then
        assertThat(phoneNumber.phone).isEqualTo(phone)
    }

    @ParameterizedTest
    @DisplayName("휴대폰 번호 생성 실패 테스트")
    @ValueSource(strings = ["", " ", "0101234567", "010123456789", "010-1324-2314", "02)1588-1588"])
    fun createFailTest(testPhone: String) {
        // given, when, then
        Assertions.assertThatThrownBy {
            PhoneNumber(testPhone)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
