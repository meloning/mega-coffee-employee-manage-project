package com.meloning.megaCoffee.domain.education

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

    @Test
    @DisplayName("교육 프로그램 상세 조회 API")
    fun detailTest() {
        // given
        val educationId = 1L

        val education = Education(educationId, Name("테스트 교육"), "어쩌구", mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME))
        val createdEducation = educationRepository.save(education)
        val educationAddress = EducationAddress(null, createdEducation, Address.DUMMY, 3, 0, LocalDate.now(), TimeRange.DUMMY)
        educationRepository.update(createdEducation.apply { update(EducationAddresses(mutableListOf(educationAddress))) })

        // when
        val response = EducationSteps.상세_요청(educationId)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
                assertThat(response.jsonPath().getLong("id")).isEqualTo(educationId)
                assertThat(response.jsonPath().getString("name")).isEqualTo(education.name.value)
                assertThat(response.jsonPath().getString("content")).isEqualTo(education.content)
                assertThat(response.jsonPath().getList<String>("targetTypes")).isNotNull
                assertThat(response.jsonPath().getList<String>("targetTypes")).contains(*education.targetTypes.map { type -> type.name }.toTypedArray())
                assertThat(response.jsonPath().getLong("educationAddresses[0].id")).isEqualTo(1)
                assertThat(response.jsonPath().getString("educationAddresses[0].address.city")).isEqualTo(Address.DUMMY.city)
                assertThat(response.jsonPath().getString("educationAddresses[0].address.street")).isEqualTo(Address.DUMMY.street)
                assertThat(response.jsonPath().getString("educationAddresses[0].address.zipCode")).isEqualTo(Address.DUMMY.zipCode)
                assertThat(response.jsonPath().getLong("educationAddresses[0].maxParticipant")).isEqualTo(3)
                assertThat(response.jsonPath().getString("educationAddresses[0].date")).isEqualTo(LocalDate.now().toString())
                assertThat(response.jsonPath().getString("educationAddresses[0].timeRange.startTime")).isEqualTo(TimeRange.DUMMY.startTime.toString())
                assertThat(response.jsonPath().getString("educationAddresses[0].timeRange.endTime")).isEqualTo(TimeRange.DUMMY.endTime.withNano(0).toString())
                assertThat(response.jsonPath().getString("createdAt")).isNotNull
                assertThat(response.jsonPath().getString("updatedAt")).isNotNull
            }
        }
    }

    @Test
    @DisplayName("교육장소 등록 API")
    fun registerEducationPlaceTest() {
        // given
        val educationId = 1L
        val education = Education(educationId, Name("테스트 교육"), "어쩌구", mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME))
        val createdEducation = educationRepository.save(education)

        val request = EducationSteps.교육장소_생성()

        // when
        val response = EducationSteps.교육장소_생성_요청(createdEducation.id!!, request)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value())
                assertThat(response.header(MDCFilter.RESPONSE_TRACE_NAME)).isNotNull
            }
        }
    }
}
