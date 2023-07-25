package com.meloning.megaCoffee.core.domain.common

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource

class NameTest {
    @Test
    @DisplayName("이름 생성 테스트")
    fun createTest() {
        // given, when
        val name = Name("장준수")

        assertThat(name.name).isEqualTo("장준수")
    }

    @ParameterizedTest
    @DisplayName("이름 생성 실패 테스트")
    @EmptySource
    @ValueSource(strings = [" "])
    fun createFailTest(name: String) {
        // given, when, then
        Assertions.assertThatThrownBy {
            Name(name)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
