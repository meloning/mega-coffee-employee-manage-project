package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.common.constant.Constant
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.filter.MDCFilter.Companion.RESPONSE_TRACE_NAME
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDate

class UserApiTest : ApiTest() {

    @Autowired
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var storeRepository: IStoreRepository

    @Autowired
    private lateinit var educationRepository: IEducationRepository

    @BeforeEach
    fun init() {
        val stores = storeRepository.saveAll(
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
        val education = Education(1, Name("테스트 교육"), "어쩌구", mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME))
        val createdEducation = educationRepository.save(education)
        val educationAddress = EducationAddress(null, createdEducation, Address.DUMMY, 3, LocalDate.now(), TimeRange.DUMMY)
        educationRepository.update(createdEducation.apply { update(EducationAddresses(mutableListOf(educationAddress))) })
        storeRepository.update(stores.first().apply { addEducation(createdEducation.id!!) })
    }

    @Test
    @DisplayName("유저 스크롤 리스트 API")
    fun scrollTest() {
        // given
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

    @Test
    @DisplayName("유저 상세 조회 API")
    fun detailTest() {
        // given
        val userId = 1L

        // when
        val response = UserSteps.상세_요청(userId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getLong("id")).isEqualTo(1)
                assertThat(response.jsonPath().getString("email")).isEqualTo(Constant.EMPTY)
                assertThat(response.jsonPath().getString("name")).isEqualTo("메로닝1")
                assertThat(response.jsonPath().getString("address.city")).isEqualTo("도시")
                assertThat(response.jsonPath().getString("address.street")).isEqualTo("거리")
                assertThat(response.jsonPath().getString("address.zipCode")).isEqualTo("12345")
                assertThat(response.jsonPath().getString("employeeType")).isEqualTo(EmployeeType.MANAGER.name)
                assertThat(response.jsonPath().getString("phoneNumber")).isEqualTo(PhoneNumber.DUMMY.phone)
                assertThat(response.jsonPath().getString("workTimeType")).isEqualTo(WorkTimeType.WEEKDAY.name)
                assertThat(response.jsonPath().getLong("store.id")).isEqualTo(1)
                assertThat(response.jsonPath().getString("store.name")).isEqualTo("메가커피 서대문역점")
                assertThat(response.jsonPath().getString("store.type")).isEqualTo(StoreType.FRANCHISE.name)
                assertThat(response.jsonPath().getString("store.address.city")).isEqualTo("도시")
                assertThat(response.jsonPath().getString("store.address.street")).isEqualTo("거리")
                assertThat(response.jsonPath().getString("store.address.zipCode")).isEqualTo("12345")
                assertThat(response.jsonPath().getString("store.timeRange.startTime")).isEqualTo(TimeRange.DUMMY.startTime.toString())
                assertThat(response.jsonPath().getString("store.timeRange.endTime")).isEqualTo(TimeRange.DUMMY.endTime.withNano(0).toString())
                assertThat(response.jsonPath().getBoolean("store.deleted")).isFalse
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    @Test
    @DisplayName("유저 생성 API")
    fun createTest() {
        // given
        val storeId = 1L
        val request = UserSteps.생성(storeId)

        // when
        val response = UserSteps.생성_요청(request)
        val userId = response.jsonPath().getLong("id")

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/users/$userId")
                assertThat(userId).isEqualTo(8)
                assertThat(response.jsonPath().getString("email")).isEqualTo(request.email)
                assertThat(response.jsonPath().getString("name")).isEqualTo(request.name)
                assertThat(response.jsonPath().getString("address.city")).isEqualTo(request.address.city)
                assertThat(response.jsonPath().getString("address.street")).isEqualTo(request.address.street)
                assertThat(response.jsonPath().getString("address.zipCode")).isEqualTo(request.address.zipCode)
                assertThat(response.jsonPath().getString("employeeType")).isEqualTo(request.employeeType.name)
                assertThat(response.jsonPath().getString("phoneNumber")).isEqualTo(request.phoneNumber)
                assertThat(response.jsonPath().getString("workTimeType")).isEqualTo(request.workTimeType.name)
                assertThat(response.jsonPath().getLong("store.id")).isEqualTo(storeId)
                assertThat(response.jsonPath().getString("store.name")).isEqualTo("메가커피 서대문역점")
                assertThat(response.jsonPath().getString("store.type")).isEqualTo(StoreType.FRANCHISE.name)
                assertThat(response.jsonPath().getString("store.address.city")).isEqualTo("도시")
                assertThat(response.jsonPath().getString("store.address.street")).isEqualTo("거리")
                assertThat(response.jsonPath().getString("store.address.zipCode")).isEqualTo("12345")
                assertThat(response.jsonPath().getString("store.timeRange.startTime")).isEqualTo(TimeRange.DUMMY.startTime.toString())
                assertThat(response.jsonPath().getString("store.timeRange.endTime")).isEqualTo(TimeRange.DUMMY.endTime.withNano(0).toString())
                assertThat(response.jsonPath().getBoolean("store.deleted")).isFalse
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    @Test
    @DisplayName("유저 교육장소 등록 API")
    fun registerEducationPlaceTest() {
        // given
        val userId = 1L
        val request = UserSteps.교육장소_등록()

        // when
        val response = UserSteps.교육장소_등록_요청(userId, request)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }

    @Test
    @DisplayName("유저 정보 변경 API")
    fun updateTest() {
        // given
        val userId = 1L
        val request = UserSteps.수정()

        // when
        val response = UserSteps.수정_요청(userId, request)

        // then
        val updatedUser = userRepository.findByIdOrThrow(userId)

        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
                assertThat(updatedUser.homeAddress.city).isEqualTo(request.address?.city)
                assertThat(updatedUser.homeAddress.street).isEqualTo(request.address?.street)
                assertThat(updatedUser.homeAddress.zipCode).isEqualTo(request.address?.zipCode)
                assertThat(updatedUser.employeeType).isEqualTo(request.employeeType)
                assertThat(updatedUser.updatedAt).isNotNull
            }
        }
    }

    @Test
    @DisplayName("유저 삭제 API")
    fun deleteTest() {
        // given
        val userId = 1L

        // when
        val response = UserSteps.삭제_요청(userId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }
}
