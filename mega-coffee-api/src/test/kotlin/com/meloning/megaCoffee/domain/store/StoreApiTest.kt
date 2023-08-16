package com.meloning.megaCoffee.domain.store

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.domain.education.EducationSteps
import com.meloning.megaCoffee.filter.MDCFilter
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDate

class StoreApiTest : ApiTest() {

    @Autowired
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var storeRepository: IStoreRepository

    @Autowired
    private lateinit var educationRepository: IEducationRepository

    @BeforeEach
    fun init() {
        // TODO: Refactoring Target
        storeRepository.saveAll(
            listOf(
                Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false),
                Store(2, Name("메가커피 구로점"), StoreType.COMPANY_OWNED, true),
                Store(3, Name("메가커피 서울대입구점"), StoreType.COMPANY_OWNED, false),
                Store(4, Name("메가커피 개봉점"), StoreType.FRANCHISE, false),
                Store(5, Name("메가커피 봉천점"), StoreType.COMPANY_OWNED, false),
                Store(6, Name("메가커피 신림점"), StoreType.COMPANY_OWNED, false),
                Store(7, Name("메가커피 신대방점"), StoreType.FRANCHISE, true),
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
        val educationAddress = EducationAddress(null, createdEducation, Address.DUMMY, 3, 0, LocalDate.now(), TimeRange.DUMMY)
        educationRepository.update(createdEducation.apply { update(EducationAddresses(mutableListOf(educationAddress))) })
    }

    /**
     * Given: 테스트를 위한 매장들을 생성한 후,
     * When: 매장 리스트를 조회하면
     * Then: 스크롤 형식으로 매장 정보들이 조회된다.
     */
    @Test
    @DisplayName("매장 스크롤 리스트 API")
    fun scrollTest() {
        // when
        val response = StoreSteps.스크롤_요청(pageable = PageRequest.of(0, 5))

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getBoolean("first")).isTrue
                assertThat(response.jsonPath().getBoolean("last")).isTrue
                assertThat(response.jsonPath().getInt("size")).isEqualTo(5)
                assertThat(response.jsonPath().getInt("number")).isEqualTo(0)
                assertThat(response.jsonPath().getInt("numberOfElements")).isEqualTo(5)
            }
        }
    }

    /**
     * Given: 매장을 생성하고 이수해야할 교육 프로그램을 등록한 후,
     * When: 해당 매장을 상세 조회하면
     * Then: 매장의 상세 정보들이 모두 조회된다.
     */
    @Test
    @DisplayName("매장 상세 조회 API")
    fun detailTest() {
        // given
        val createRequest = StoreSteps.생성()
        val storeId = StoreSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val registerStoreRequest = EducationSteps.매장_등록(storeId)
        EducationSteps.매장_등록_요청(1L, registerStoreRequest)

        // when
        val response = StoreSteps.상세_요청(storeId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getLong("id")).isEqualTo(storeId)
                assertThat(response.jsonPath().getString("name")).isEqualTo(createRequest.name)
                assertThat(response.jsonPath().getString("type")).isEqualTo(createRequest.type.name)
                assertThat(response.jsonPath().getString("ownerId")).isNull()
                assertThat(response.jsonPath().getString("address.city")).isEqualTo(createRequest.address.city)
                assertThat(response.jsonPath().getString("address.street")).isEqualTo(createRequest.address.street)
                assertThat(response.jsonPath().getString("address.zipCode")).isEqualTo(createRequest.address.zipCode)
                assertThat(response.jsonPath().getString("timeRange.startTime")).isEqualTo(createRequest.timeRange.startTime)
                assertThat(response.jsonPath().getString("timeRange.endTime")).isEqualTo(createRequest.timeRange.endTime)
                assertThat(response.jsonPath().getList<Any>("educations").size).isEqualTo(1)
                assertThat(response.jsonPath().getList<Any>("educations[0].educationAddresses").size).isEqualTo(1)
                assertThat(response.jsonPath().getLong("educations[0].educationAddresses[0].maxParticipant")).isEqualTo(3)
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    /**
     * When: 매장 생성 요청을 하면
     * Then: 신규 매장이 생성된다.
     */
    @Test
    @DisplayName("매장 생성 API")
    fun createTest() {
        // given
        val request = StoreSteps.생성()

        // when
        val response = StoreSteps.생성_요청(request)
        val storeId = response.jsonPath().getLong("id")

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/stores/$storeId")
                assertThat(storeId).isEqualTo(8)
                assertThat(response.jsonPath().getString("name")).isEqualTo(request.name)
                assertThat(response.jsonPath().getString("type")).isEqualTo(request.type.name)
                assertThat(response.jsonPath().getString("address.city")).isEqualTo(request.address.city)
                assertThat(response.jsonPath().getString("address.street")).isEqualTo(request.address.street)
                assertThat(response.jsonPath().getString("address.zipCode")).isEqualTo(request.address.zipCode)
                assertThat(response.jsonPath().getString("timeRange.startTime")).isEqualTo(TimeRange.DUMMY.startTime.toString())
                assertThat(response.jsonPath().getString("timeRange.endTime")).isEqualTo(TimeRange.DUMMY.endTime.withNano(0).toString())
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    /**
     * Given: 매장을 생성한 후,
     * When: 생성한 매장의 정보 변경을 요청하면
     * Then: 매장의 정보들이 변경된다.
     */
    @Test
    @DisplayName("매장 정보 변경 API")
    fun updateTest() {
        // given
        val createRequest = StoreSteps.생성()
        val storeId = StoreSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val request = StoreSteps.수정()

        // when
        val response = StoreSteps.수정_요청(storeId, request)

        // then
        val updatedStore = storeRepository.findByIdOrThrow(storeId)

        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(updatedStore.type).isEqualTo(request.type)
                assertThat(updatedStore.ownerId).isNull()
                assertThat(updatedStore.address.city).isEqualTo(request.address?.city)
                assertThat(updatedStore.address.street).isEqualTo(request.address?.street)
                assertThat(updatedStore.address.zipCode).isEqualTo(request.address?.zipCode)
                assertThat(updatedStore.timeRange.startTime).isEqualTo(request.timeRange?.startTime)
                assertThat(updatedStore.timeRange.endTime).isEqualTo(request.timeRange?.endTime)
                assertThat(updatedStore.updatedAt).isNotNull
            }
        }
    }

    /**
     * Given: 매장을 생성한 후,
     * When: 해당 매장 삭제 요청을 하면
     * Then: 매장은 삭제된다.
     */
    @Test
    @DisplayName("매장 삭제 API")
    fun deleteTest() {
        // given
        val createRequest = StoreSteps.생성()
        val storeId = StoreSteps.생성_요청(createRequest).jsonPath().getLong("id")

        // when
        val response = StoreSteps.삭제_요청(storeId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }
}
