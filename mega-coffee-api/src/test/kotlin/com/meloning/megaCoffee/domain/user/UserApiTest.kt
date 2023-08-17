package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IStoreEducationRelationRepository
import com.meloning.megaCoffee.core.domain.relation.repository.IUserEducationPlaceRelationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.repository.findByIdOrThrow
import com.meloning.megaCoffee.domain.education.EducationSteps
import com.meloning.megaCoffee.domain.store.StoreSteps
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
    private lateinit var storeEducationRelationRepository: IStoreEducationRelationRepository

    @Autowired
    private lateinit var userEducationRelationRepository: IUserEducationPlaceRelationRepository

    @Autowired
    private lateinit var educationRepository: IEducationRepository

    @BeforeEach
    fun init() {
        // TODO: Refactoring Target
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
        val educationPlace = EducationPlace(1, createdEducation, Address.DUMMY, 3, 0, LocalDate.now(), TimeRange.DUMMY)
        educationRepository.update(createdEducation.apply { update(EducationPlaces(mutableListOf(educationPlace))) })
        storeEducationRelationRepository.save(StoreEducationRelation.create(storeId = stores.first().id!!, education = createdEducation))
    }

    /**
     * Given: 테스트를 위한 유저들을 생성한 후,
     * When: 유저 리스트를 조회하면
     * Then: 스크롤 형식으로 유저 정보들이 조회된다.
     */
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

    /**
     * Given: 신규 매장 및 유저를 생성하고 교육 장소 참여자로 등록한 후,
     * When: 해당 유저를 상세 조회하면
     * Then: 유저의 상세 정보들이 모두 조회된다.
     */
    @Test
    @DisplayName("유저 상세 조회 API")
    fun detailTest() {
        // given
        val createStoreRequest = StoreSteps.생성()
        val storeId = StoreSteps.생성_요청(createStoreRequest).jsonPath().getLong("id")

        val registerStoreRequest = EducationSteps.매장_등록(storeId)
        EducationSteps.매장_등록_요청(1L, registerStoreRequest)

        val createUserRequest = UserSteps.생성(storeId)
        val userId = UserSteps.생성_요청(createUserRequest).jsonPath().getLong("id")

        EducationSteps.유저_교육장소_등록_요청(1, userId, EducationSteps.유저_교육장소_등록())

        // when
        val response = UserSteps.상세_요청(userId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getLong("id")).isEqualTo(userId)
                assertThat(response.jsonPath().getString("email")).isEqualTo(createUserRequest.email)
                assertThat(response.jsonPath().getString("name")).isEqualTo(createUserRequest.name)
                assertThat(response.jsonPath().getString("address.city")).isEqualTo(createUserRequest.address.city)
                assertThat(response.jsonPath().getString("address.street")).isEqualTo(createUserRequest.address.street)
                assertThat(response.jsonPath().getString("address.zipCode")).isEqualTo(createUserRequest.address.zipCode)
                assertThat(response.jsonPath().getString("employeeType")).isEqualTo(createUserRequest.employeeType.name)
                assertThat(response.jsonPath().getString("phoneNumber")).isEqualTo(createUserRequest.phoneNumber)
                assertThat(response.jsonPath().getString("workTimeType")).isEqualTo(createUserRequest.workTimeType.name)
                assertThat(response.jsonPath().getLong("store.id")).isEqualTo(storeId)
                assertThat(response.jsonPath().getString("store.name")).isEqualTo(createStoreRequest.name)
                assertThat(response.jsonPath().getString("store.type")).isEqualTo(createStoreRequest.type.name)
                assertThat(response.jsonPath().getString("store.address.city")).isEqualTo(createStoreRequest.address.city)
                assertThat(response.jsonPath().getString("store.address.street")).isEqualTo(createStoreRequest.address.street)
                assertThat(response.jsonPath().getString("store.address.zipCode")).isEqualTo(createStoreRequest.address.zipCode)
                assertThat(response.jsonPath().getString("store.timeRange.startTime")).isEqualTo(createStoreRequest.timeRange.startTime)
                assertThat(response.jsonPath().getString("store.timeRange.endTime")).isEqualTo(createStoreRequest.timeRange.endTime)
                assertThat(response.jsonPath().getBoolean("store.deleted")).isFalse
                assertThat(response.jsonPath().getList<Any>("educations").size).isEqualTo(1)
                assertThat(response.jsonPath().getList<Any>("educations[0].educationPlaces").size).isEqualTo(1)
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    /**
     * When: 유저 생성 요청을 하면
     * Then: 새로운 유저가 생성된다.
     */
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

    /**
     * Given: 유저를 생성한 후,
     * When: 생성한 유저의 정보 변경을 요청하면
     * Then: 유저의 정보들이 변경된다.
     */
    @Test
    @DisplayName("유저 정보 변경 API")
    fun updateTest() {
        // given
        val createUserRequest = UserSteps.생성(1)
        val userId = UserSteps.생성_요청(createUserRequest).jsonPath().getLong("id")

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

    /**
     * Given: 유저를 생성한 후,
     * When: 해당 유저 삭제 요청을 하면
     * Then: 유저는 삭제된다.
     */
    @Test
    @DisplayName("유저 삭제 API")
    fun deleteTest() {
        // given
        val createUserRequest = UserSteps.생성(1)
        val userId = UserSteps.생성_요청(createUserRequest).jsonPath().getLong("id")

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
