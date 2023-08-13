package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.filter.MDCFilter.Companion.RESPONSE_TRACE_NAME
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus

class UserApiTest : ApiTest() {

    @Autowired
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var storeRepository: IStoreRepository

    @Test
    @DisplayName("유저 리스트 스크롤 테스트")
    fun scrollTest() {
        // given
        storeRepository.saveAll(
            listOf(
                Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false),
                Store(2, Name("메가커피 구로점"), StoreType.COMPANY_OWNED, true),
            )
        )
        userRepository.saveAll(
            listOf(
                User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1),
                User(2, Name("메로닝2"), EmployeeType.PART_TIME, WorkTimeType.WEEKDAY, 2),
                User(3, Name("메로닝3"), EmployeeType.MANAGER, WorkTimeType.WEEKEND, 1),
                User(4, Name("메로닝4"), EmployeeType.SUPER_MANAGER, WorkTimeType.WEEKDAY, 2),
                User(5, Name("메로닝5"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1),
                User(6, Name("메로닝6"), EmployeeType.PART_TIME, WorkTimeType.WEEKEND, 2),
                User(7, Name("메로닝7"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1),
            )
        )

        val request = UserSteps.스크롤()

        // when
        val response = UserSteps.스크롤_요청(request, PageRequest.of(0, 5))

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getBoolean("first")).isTrue
                assertThat(response.jsonPath().getBoolean("last")).isFalse
                assertThat(response.jsonPath().getInt("size")).isEqualTo(5)
                assertThat(response.jsonPath().getInt("number")).isEqualTo(0)
                assertThat(response.jsonPath().getInt("numberOfElements")).isEqualTo(5)
            }
        }
    }
}
