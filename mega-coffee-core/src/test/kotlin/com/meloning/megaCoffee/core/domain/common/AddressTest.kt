package com.meloning.megaCoffee.core.domain.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AddressTest {

    @Test
    @DisplayName("주소 생성 테스트")
    fun createTest() {
        // given
        val city = "서울특별시"
        val street = "강남구 역삼동"
        val zipCode = "123-45"

        // when
        val address = Address(city, street, zipCode)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(address.city).isEqualTo(city)
                assertThat(address.street).isEqualTo(street)
                assertThat(address.zipCode).isEqualTo(zipCode)
            }
        }
    }

    @ParameterizedTest
    @DisplayName("주소 생성 실패 테스트")
    @CsvSource(
        ", 강남구, 14-324",
        "   , 강남구, 14-324",
        "서울특별시, ,12-324",
        "서울특별시,    ,12-324",
        "서울특별시, 강남구 역삼동, ",
        "서울특별시, 강남구 역삼동,    ",
    )
    fun createFailTest(city: String?, street: String?, zipCode: String?) {
        // given, when, then
        Assertions.assertThatThrownBy {
            Address(city.orEmpty(), street.orEmpty(), zipCode.orEmpty())
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
