package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.common.constant.Constant
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation
import com.meloning.megaCoffee.core.domain.relation.repository.IStoreEducationRelationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.filter.MDCFilter
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDate

class EducationApiTest : ApiTest() {

    @Autowired
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var storeRepository: IStoreRepository

    @Autowired
    private lateinit var educationRepository: IEducationRepository

    @Autowired
    private lateinit var storeEducationRelationRepository: IStoreEducationRelationRepository

    @BeforeEach
    fun init() {
        // TODO: Refactoring Target
        storeRepository.saveAll(
            listOf(
                Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false),
                Store(2, Name("메가커피 구로점"), StoreType.COMPANY_OWNED, true)
            )
        )
        userRepository.saveAll(
            listOf(
                User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1),
                User(2, Name("메로닝2"), EmployeeType.PART_TIME, WorkTimeType.WEEKDAY, 2)
            )
        )
    }

    /**
     * When: 교육 프로그램을 생성 요청을 하면
     * Then: 교육 프로그램이 생성된다.
     */
    @Test
    @DisplayName("교육 프로그램 생성 API")
    fun createTest() {
        // given
        val request = EducationSteps.생성()

        // when
        val response = EducationSteps.생성_요청(request)

        // then
        val educationId = response.jsonPath().getLong("id")

        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/educations/$educationId")
                assertThat(educationId).isEqualTo(1)
                assertThat(response.jsonPath().getString("name")).isEqualTo(request.name)
                assertThat(response.jsonPath().getString("content")).isEqualTo(request.content)
                assertThat(response.jsonPath().getList<String>("targetTypes")).isNotNull
                assertThat(response.jsonPath().getList<String>("targetTypes")).contains(*request.targetTypes.map { type -> type.name }.toTypedArray())
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    /**
     * Given: 교육 프로그램을 생성하고, 장소를 등록한후
     * When: 교육 프로그램을 조회하면
     * Then: 교육 프로그램과 등록된 장소가 조회된다.
     */
    @Test
    @DisplayName("교육 프로그램 상세 조회 API")
    fun detailTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val createPlaceRequest = EducationSteps.교육장소_생성()
        EducationSteps.교육장소_생성_요청(educationId, createPlaceRequest)

        val educationPlaceRequest = createPlaceRequest.places[0]

        // when
        val response = EducationSteps.상세_요청(educationId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getLong("id")).isEqualTo(educationId)
                assertThat(response.jsonPath().getString("name")).isEqualTo(createRequest.name)
                assertThat(response.jsonPath().getString("content")).isEqualTo(createRequest.content)
                assertThat(response.jsonPath().getList<String>("targetTypes")).isNotNull
                assertThat(response.jsonPath().getList<String>("targetTypes")).contains(*createRequest.targetTypes.map { type -> type.name }.toTypedArray())
                assertThat(response.jsonPath().getLong("educationPlaces[0].id")).isEqualTo(1)
                assertThat(response.jsonPath().getString("educationPlaces[0].address.city")).isEqualTo(educationPlaceRequest.address.city)
                assertThat(response.jsonPath().getString("educationPlaces[0].address.street")).isEqualTo(educationPlaceRequest.address.street)
                assertThat(response.jsonPath().getString("educationPlaces[0].address.zipCode")).isEqualTo(educationPlaceRequest.address.zipCode)
                assertThat(response.jsonPath().getInt("educationPlaces[0].maxParticipant")).isEqualTo(educationPlaceRequest.maxParticipant)
                assertThat(response.jsonPath().getString("educationPlaces[0].date")).isEqualTo(educationPlaceRequest.date)
                assertThat(response.jsonPath().getString("educationPlaces[0].timeRange.startTime")).isEqualTo(educationPlaceRequest.timeRange.startTime)
                assertThat(response.jsonPath().getString("educationPlaces[0].timeRange.endTime")).isEqualTo(educationPlaceRequest.timeRange.endTime)
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    /**
     * Given: 교육 프로그램을 생성하고
     * When: 교육 프로그램의 장소를 등록하면
     * Then: 교육 프로그램의 장소가 등록된다.
     */
    @Test
    @DisplayName("교육장소 등록 API")
    fun registerEducationPlaceTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val request = EducationSteps.교육장소_생성()

        // when
        val response = EducationSteps.교육장소_생성_요청(educationId, request)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }

    /**
     * Given: 교육 프로그램을 생성하고 장소를 등록한 후,
     * When: 교육 프로그램을 이수할 매장을 선택하면
     * Then: 매장은 교육 프로그램을 필수로 들을 수 있도록 등록된다.
     */
    @Test
    @DisplayName("교육 프로그램 이수할 매장 등록 API")
    fun registerStoreTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val createAddressRequest = EducationSteps.교육장소_생성()
        EducationSteps.교육장소_생성_요청(educationId, createAddressRequest)

        val request = EducationSteps.매장_등록()

        // when
        val response = EducationSteps.매장_등록_요청(educationId, request)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }

    /**
     * Given: 교육 프로그램 생성 및 장소를 등록한 후,
     * When: 참여자인 인증된 유저가 장소를 선택하면
     * Then: 선택한 장소에 유저는 등록된다.
     */
    @Test
    @DisplayName("교육장소의 현재 참여자 등록 API")
    fun registerParticipantTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val createAddressRequest = EducationSteps.교육장소_생성()
        EducationSteps.교육장소_생성_요청(educationId, createAddressRequest)

        val education = educationRepository.findByIdOrThrow(educationId)
        storeEducationRelationRepository.save(StoreEducationRelation.create(1L, education))

        val userId = 1L

        val request = EducationSteps.유저_교육장소_등록()

        // when
        val response = EducationSteps.유저_교육장소_등록_요청(educationId, userId, request)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }

    /**
     * Given: 교육 프로그램 생성 및 장소를 등록한 후,
     * When: 요청 날짜를 입력하면,
     * Then: 해당 날짜의 교육장소들 정보를 알려준다.
     */
    @Test
    @DisplayName("특정 날짜의 교육장소 리스트 API")
    fun getEducationPlacesByDateTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val createAddressRequest = EducationSteps.교육장소_생성()
        EducationSteps.교육장소_생성_요청(educationId, createAddressRequest)

        // when
        val response = EducationSteps.특정날짜의_교육장소_리스트_요청(LocalDate.now())

        // then
        val requestPlaces = createAddressRequest.places.first()
        val educationPlaceAddress = requestPlaces.address.toModel()
        val educationPlaceTimeRange = requestPlaces.timeRange.toModel()

        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getList("id", Long::class.java)).contains(1)
                assertThat(response.jsonPath().getList("educationName", String::class.java)).contains(createRequest.name)
                assertThat(response.jsonPath().getList("address", String::class.java)).contains(educationPlaceAddress.getAddress())
                assertThat(response.jsonPath().getList("date", String::class.java)).contains(LocalDate.now().toString())
                assertThat(response.jsonPath().getList("time", String::class.java)).contains(educationPlaceTimeRange.toString())
                assertThat(response.jsonPath().getList("maxParticipant", Int::class.java)).contains(requestPlaces.maxParticipant)
                assertThat(response.jsonPath().getList("currentParticipant", Int::class.java)).contains(0)
                assertThat(response.jsonPath().getList("createdAt", String::class.java)).isNotNull
                assertThat(response.jsonPath().getList("updatedAt", String::class.java)).isNotNull
            }
        }
    }

    /**
     * Given:
     *   1. 교육 프로그램 생성 및 장소 등록
     *   2. 매장이 이수해야할 교육 프로그램 등록
     *   3. 교육 장소 참여자 등록
     * When: 해당 교육장소의 참여자들 정보를 요청하면
     * Then: 현재 참여자들의 정보를 알 수 있다.
     */
    @Test
    @DisplayName("특정 교육장소의 참여자 리스트 API")
    fun getParticipantsByEducationPlaceTest() {
        // given
        val createRequest = EducationSteps.생성()
        val educationId = EducationSteps.생성_요청(createRequest).jsonPath().getLong("id")

        val createAddressRequest = EducationSteps.교육장소_생성()
        EducationSteps.교육장소_생성_요청(educationId, createAddressRequest)

        val createEducationStoreRequest = EducationSteps.매장_등록()
        EducationSteps.매장_등록_요청(educationId, createEducationStoreRequest)

        val creatPlaceUserRequest = EducationSteps.유저_교육장소_등록()
        EducationSteps.유저_교육장소_등록_요청(educationId, 1, creatPlaceUserRequest)

        // when
        val response = EducationSteps.교육장소의_현재_참여자_리스트_요청(1)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getList("id", Long::class.java)).contains(1)
                assertThat(response.jsonPath().getList("name", String::class.java)).contains("메로닝1")
                assertThat(response.jsonPath().getList("email", String::class.java)).contains(Constant.EMPTY)
            }
        }
    }
}
