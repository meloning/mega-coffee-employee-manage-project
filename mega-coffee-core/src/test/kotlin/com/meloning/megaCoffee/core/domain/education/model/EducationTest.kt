package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.exception.ConflictFieldException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EducationTest {

    @Test
    @DisplayName("유저가 해당 교육 프로그램 대상자인지 테스트")
    fun validateUserTest() {
        // given
        val education = Education(
            id = 1,
            name = Name("테스트"),
            content = "테스트라구 테스트",
            targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME),
            educationAddresses = EducationAddresses(mutableListOf()),
        )

        // when, then
        Assertions.assertThatThrownBy {
            education.validateUserEligibility(EmployeeType.SUPER_MANAGER)
        }.isInstanceOf(ConflictFieldException::class.java)
            .hasMessage("해당 유저는 테스트 교육 프로그램 대상자가 아닙니다.")
    }
}
