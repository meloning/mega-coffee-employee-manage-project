package com.meloning.megaCoffee.domain.store

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
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.usecase.StoreService
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import com.meloning.megaCoffee.domain.store.dto.CreateStoreRequest
import com.meloning.megaCoffee.domain.store.dto.UpdateStoreRequest
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDate
import java.time.LocalTime

@ApiRestDocsTest
class StoreApiDocsTest {

    @Mock
    private lateinit var storeService: StoreService

    @InjectMocks
    private lateinit var storeApiController: StoreApiController

    @Test
    @DisplayName("매장 스크롤 리스트 API 문서")
    fun scrollTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val scrollResponse: InfiniteScrollType<Store> = Pair(
            first = listOf(
                Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)
            ),
            second = true
        )
        val pageRequest = PageRequest.of(0, 5)
        whenever(storeService.scroll(anyOrNull(), any(), any())).thenReturn(scrollResponse)

        val requestParameters = listOf(
            RequestDocumentation.parameterWithName("page").attributes(Attributes.key("format").value("NUMBER")).description("페이지 번호").optional(),
            RequestDocumentation.parameterWithName("size").attributes(Attributes.key("format").value("NUMBER")).description("출력될 요소 개수").optional(),

            RequestDocumentation.parameterWithName("storeId").attributes(Attributes.key("format").value("STRING")).description("마지막 매장 ID").optional(),
        ).toTypedArray()

        val responseFields = listOf(
            fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("실제 데이터"),
            fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("매장 PK"),
            fieldWithPath("content[].name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("content[].type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),

            fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보").ignored(),
            fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("페이지 내 정렬 정보").ignored(),
            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬설정이 비어있는지").ignored(),
            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지").ignored(),
            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되어있지 않은지").ignored(),
            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("오프셋 값").ignored(),
            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 값").ignored(),
            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("출력될 요소 개수").ignored(),
            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 된건지").ignored(),
            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 안된건지").ignored(),

            fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),

            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬"),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬설정이 비어있는지"),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지"),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되어있지 않은지"),

            fieldWithPath("size").type(JsonFieldType.NUMBER).description("몇개까지 보이게 할건지"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("처음인지"),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막인지"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지에서 출력된 요소 개수"),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는지"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(storeApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.get("/api/v1/stores/scroll")
                    .queryParam("page", pageRequest.pageNumber.toString())
                    .queryParam("size", pageRequest.pageSize.toString())
                    .queryParam("storeId", null)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "get-stores-scroll",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestParameters(
                                *requestParameters
                            )
                            .responseFields(
                                responseFields
                            )
                            .build()
                    )
                )
            )
    }

    @Test
    @DisplayName("매장 상세 API 문서")
    fun detailTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val store = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)
        val education = Education(1, Name("테스트 프로그램"), "테스트 내용", mutableListOf(EmployeeType.PART_TIME)).apply {
            addAddress(EducationPlace(1, this, Address.DUMMY, 3, 1, LocalDate.now(), TimeRange.DUMMY))
        }
        val educations = listOf(education)

        whenever(storeService.detail(any())).thenReturn(Pair(store, educations))

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("매장 PK"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),
            fieldWithPath("ownerId").type(JsonFieldType.NUMBER).description("점주 PK").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("매장 주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간"),
            fieldWithPath("timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간"),
            fieldWithPath("timeRange.endTime").type(JsonFieldType.STRING).description("마감시간"),

            fieldWithPath("educations").type(JsonFieldType.ARRAY).description("교육 프로그램 데이터"),
            fieldWithPath("educations[].id").type(JsonFieldType.NUMBER).description("교육 프로그램 PK"),
            fieldWithPath("educations[].name").type(JsonFieldType.STRING).description("교육 프로그램 이름"),
            fieldWithPath("educations[].targetTypes").type(JsonFieldType.ARRAY)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("대상자 타입들"),

            fieldWithPath("educations[].educationPlaces").type(JsonFieldType.ARRAY).description("교육 장소 데이터"),
            fieldWithPath("educations[].educationPlaces[].id").type(JsonFieldType.NUMBER).description("교육 장소 PK"),
            fieldWithPath("educations[].educationPlaces[].address").type(JsonFieldType.OBJECT).description("교육 장소 주소"),
            fieldWithPath("educations[].educationPlaces[].address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("educations[].educationPlaces[].address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("educations[].educationPlaces[].address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("educations[].educationPlaces[].maxParticipant").type(JsonFieldType.NUMBER).description("최대 참여자 수"),
            fieldWithPath("educations[].educationPlaces[].date").type(JsonFieldType.STRING).description("날짜"),
            fieldWithPath("educations[].educationPlaces[].timeRange").type(JsonFieldType.OBJECT).description("운영시간"),
            fieldWithPath("educations[].educationPlaces[].timeRange.startTime").type(JsonFieldType.STRING).description("시작시간"),
            fieldWithPath("educations[].educationPlaces[].timeRange.endTime").type(JsonFieldType.STRING).description("종료시간"),

            fieldWithPath("createdAt").type(JsonFieldType.STRING).optional().description("생성일"),
            fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("변경일"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(storeApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.get("/api/v1/stores/{id}", 1)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "get-stores-detail",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .responseFields(
                                responseFields
                            )
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("매장 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("매장 생성 API 문서")
    fun createTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockStore = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)

        whenever(storeService.create(any())).thenReturn(mockStore)

        val createStoreRequest = CreateStoreRequest(
            name = "메가커피 서대문역점",
            type = StoreType.FRANCHISE,
            address = AddressRequest("어느 도시", "어느 거리", "12345"),
            timeRange = TimeRangeRequest(LocalTime.MIN.toString(), LocalTime.MAX.withNano(0).toString())
        )
        val jsonCreateStoreRequest = ObjectMapperUtils.toPrettyJson(createStoreRequest)

        val requestFields = listOf(
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간"),
            fieldWithPath("timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간"),
            fieldWithPath("timeRange.endTime").type(JsonFieldType.STRING).description("마감시간"),
        )

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("매장 PK"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),

            fieldWithPath("address").type(JsonFieldType.OBJECT).description("매장 주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),

            fieldWithPath("timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간"),
            fieldWithPath("timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간"),
            fieldWithPath("timeRange.endTime").type(JsonFieldType.STRING).description("마감시간"),

            fieldWithPath("createdAt").type(JsonFieldType.STRING).optional().description("생성일"),
            fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("변경일"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(storeApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/stores")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonCreateStoreRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-stores",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Redirect할 유저 상세 API Url Path")
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
    @DisplayName("매장 수정 API 문서")
    fun updateTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockStore = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)
        whenever(storeService.update(any(), any())).thenReturn(mockStore)

        val updateStoreRequest = UpdateStoreRequest(
            type = StoreType.COMPANY_OWNED,
            ownerId = 1,
            address = null,
            timeRange = null
        )
        val jsonUpdateStoreRequest = ObjectMapperUtils.toPrettyJson(updateStoreRequest)

        val requestFields = listOf(
            fieldWithPath("type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입").optional(),
            fieldWithPath("ownerId").type(JsonFieldType.NUMBER).description("점주 PK").optional(),

            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소").optional(),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시").optional(),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리").optional(),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호").optional(),

            fieldWithPath("timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간").optional(),
            fieldWithPath("timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간").optional(),
            fieldWithPath("timeRange.endTime").type(JsonFieldType.STRING).description("마감시간").optional(),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(storeApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.put("/api/v1/stores/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonUpdateStoreRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "put-stores",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(requestFields)
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("매장 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("매장 삭제 API 문서")
    fun deleteTest(contextProvider: RestDocumentationContextProvider) {
        // given
        doNothing().`when`(storeService).delete(any())

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(storeApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.delete("/api/v1/stores/{id}", 1)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "delete-stores",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("매장 PK")
                    ),
                )
            )
    }
}
