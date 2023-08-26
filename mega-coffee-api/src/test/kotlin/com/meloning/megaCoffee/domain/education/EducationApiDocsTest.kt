package com.meloning.megaCoffee.domain.education

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.meloning.megaCoffee.common.util.ObjectMapperUtils
import com.meloning.megaCoffee.config.document.ApiRestDocsTest
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlaceParticipantRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlacesRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterStoresRequest
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDate
import java.time.LocalTime

@ApiRestDocsTest
class EducationApiDocsTest {

    @Mock
    private lateinit var educationService: EducationService

    @Mock
    private lateinit var registerParticipantFacadeService: RegisterParticipantFacadeService

    @InjectMocks
    private lateinit var educationApiController: EducationApiController

    @Test
    @DisplayName("교육프로그램 생성 API 문서")
    fun createTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockEducation = Education(1, Name("테스트 프로그램"), "테스트 내용", mutableListOf(EmployeeType.PART_TIME))

        whenever(educationService.create(any())).thenReturn(mockEducation)

        val createEducationRequest = CreateEducationRequest(
            name = "테스트 교육 프로그램",
            content = "테스트 교육",
            targetTypes = listOf(EmployeeType.PART_TIME)
        )
        val jsonCreateEducationRequest = ObjectMapperUtils.toPrettyJson(createEducationRequest)

        val requestFields = listOf(
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
            fieldWithPath("targetTypes").type(JsonFieldType.ARRAY)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("대상자 타입들")
        )

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("교육 PK"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("교육 이름"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("교육 설명"),
            fieldWithPath("targetTypes").type(JsonFieldType.ARRAY)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("대상자 타입들"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).optional().description("생성일"),
            fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("변경일"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(educationApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/educations")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonCreateEducationRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-educations",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .responseHeaders(
                                HeaderDocumentation.headerWithName(HttpHeaders.LOCATION).description("Redirect할 교육 상세 API Url Path")
                            )
                            .responseFields(
                                responseFields
                            )
                            .build()
                    ),

                )
            )
    }

    @Test
    @DisplayName("교육 프로그램 상세 API 문서")
    fun detailTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockEducation = Education(1, Name("테스트 프로그램"), "테스트 내용", mutableListOf(EmployeeType.PART_TIME)).apply {
            addAddress(EducationPlace(1, this, Address.DUMMY, 3, 1, LocalDate.now(), TimeRange.DUMMY))
        }

        whenever(educationService.detail(any())).thenReturn(mockEducation)

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("교육 PK"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("교육 이름"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("교육 설명"),
            fieldWithPath("targetTypes").type(JsonFieldType.ARRAY)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("대상자 타입들"),

            fieldWithPath("educationPlaces").type(JsonFieldType.ARRAY).description("교육 장소 데이터"),
            fieldWithPath("educationPlaces[].id").type(JsonFieldType.NUMBER).description("교육 장소 PK"),
            fieldWithPath("educationPlaces[].address").type(JsonFieldType.OBJECT).description("교육 장소 주소"),
            fieldWithPath("educationPlaces[].address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("educationPlaces[].address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("educationPlaces[].address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("educationPlaces[].maxParticipant").type(JsonFieldType.NUMBER).description("최대 참여자 수"),
            fieldWithPath("educationPlaces[].date").type(JsonFieldType.STRING).description("날짜"),
            fieldWithPath("educationPlaces[].timeRange").type(JsonFieldType.OBJECT).description("운영시간"),
            fieldWithPath("educationPlaces[].timeRange.startTime").type(JsonFieldType.STRING).description("시작시간"),
            fieldWithPath("educationPlaces[].timeRange.endTime").type(JsonFieldType.STRING).description("종료시간"),

            fieldWithPath("createdAt").type(JsonFieldType.STRING).optional().description("생성일"),
            fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("변경일"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(educationApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.get("/api/v1/educations/{id}", 1)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "get-educations-detail",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .responseFields(
                                responseFields
                            )
                            .build()
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("교육 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("교육 장소 등록 API 문서")
    fun registerPlaceTest(contextProvider: RestDocumentationContextProvider) {
        // given
        doNothing().`when`(educationService).registerAddress(any(), any())

        val registerEducationPlaceRequest = RegisterEducationPlacesRequest(
            places = listOf(
                RegisterEducationPlacesRequest.EducationPlaceRequest(
                    address = AddressRequest("어느 도시", "어느 거리", "12345"),
                    maxParticipant = 3,
                    date = LocalDate.now().toString(),
                    timeRange = TimeRangeRequest(LocalTime.MIN.toString(), LocalTime.MAX.withNano(0).toString())
                )
            )
        )
        val jsonRegisterEducationPlaceRequest = ObjectMapperUtils.toPrettyJson(registerEducationPlaceRequest)

        val requestFields = listOf(
            fieldWithPath("places").type(JsonFieldType.ARRAY).description("교육장소 요청 데이터"),
            fieldWithPath("places[].address").type(JsonFieldType.OBJECT).description("교육 장소 주소"),
            fieldWithPath("places[].address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("places[].address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("places[].address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("places[].maxParticipant").type(JsonFieldType.NUMBER).description("최대 참여자 수"),
            fieldWithPath("places[].date").type(JsonFieldType.STRING).description("날짜"),
            fieldWithPath("places[].timeRange").type(JsonFieldType.OBJECT).description("운영시간"),
            fieldWithPath("places[].timeRange.startTime").type(JsonFieldType.STRING).description("시작시간"),
            fieldWithPath("places[].timeRange.endTime").type(JsonFieldType.STRING).description("종료시간"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(educationApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/educations/{id}/place/register", 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRegisterEducationPlaceRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isAccepted)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-educations-places-register",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .build()
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("교육 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("교육 프로그램 이수할 매장 등록 API 문서")
    fun registerStoreTest(contextProvider: RestDocumentationContextProvider) {
        // given
        doNothing().`when`(educationService).registerStore(any(), any())

        val registerStoreRequest = RegisterStoresRequest(
            stores = listOf(1)
        )
        val jsonRegisterStoresRequest = ObjectMapperUtils.toPrettyJson(registerStoreRequest)

        val requestFields = listOf(
            fieldWithPath("stores").type(JsonFieldType.ARRAY).description("이수할 매장 PK들"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(educationApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/educations/{id}/stores/register", 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRegisterStoresRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isAccepted)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-educations-stores-register",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .build()
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("교육 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("교육 장소 참여자 등록 API 문서")
    fun registerParticipantTest(contextProvider: RestDocumentationContextProvider) {
        // given
        doNothing().`when`(registerParticipantFacadeService).execute(any(), any(), any())

        val registerEducationPlaceParticipantRequest = RegisterEducationPlaceParticipantRequest(
            educationPlaceIds = listOf(1)
        )
        val jsonRegisterParticipantRequest = ObjectMapperUtils.toPrettyJson(registerEducationPlaceParticipantRequest)

        val requestFields = listOf(
            fieldWithPath("educationPlaceIds").type(JsonFieldType.ARRAY).description("등록할 교육장소 PK들"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(educationApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/educations/{id}/places/participant/{userId}/register", 1, 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRegisterParticipantRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isAccepted)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-educations-places-participant-register",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .build()
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("교육 PK"),
                        RequestDocumentation.parameterWithName("userId").description("유저 PK"),
                    ),
                )
            )
    }
}
