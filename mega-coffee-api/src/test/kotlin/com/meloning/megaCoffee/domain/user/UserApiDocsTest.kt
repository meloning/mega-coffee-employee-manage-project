package com.meloning.megaCoffee.domain.user

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.meloning.megaCoffee.common.util.ObjectMapperUtils
import com.meloning.megaCoffee.config.document.ApiRestDocsTest
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.user.dto.CreateUserRequest
import com.meloning.megaCoffee.domain.user.dto.UpdateUserRequest
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDate

@ApiRestDocsTest
class UserApiDocsTest {

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var userApiController: UserApiController

    @Test
    @DisplayName("유저 스크롤 리스트 API 문서")
    fun scrollTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val scrollResponse: InfiniteScrollType<Pair<User, Store>> = Pair(
            first = listOf(
                Pair(
                    first = User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1),
                    second = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)
                )
            ),
            second = true
        )
        val pageRequest = PageRequest.of(0, 5)
        whenever(userService.scroll(any(), any(), any())).thenReturn(scrollResponse)

        val requestParameters = listOf(
            parameterWithName("page").attributes(Attributes.key("format").value("NUMBER")).description("페이지 번호").optional(),
            parameterWithName("size").attributes(Attributes.key("format").value("NUMBER")).description("출력될 요소 개수").optional(),

            parameterWithName("userId").attributes(Attributes.key("format").value("STRING")).description("마지막 유저 ID").optional(),
            parameterWithName("keyword").attributes(Attributes.key("format").value("STRING")).description("유저 이름 검색").optional(),
            parameterWithName("employeeType")
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입").optional(),
            parameterWithName("workTimeType")
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무요일 타입").optional(),
            parameterWithName("storeId").attributes().description("매장 ID").optional()
        ).toTypedArray()

        val responseFields = listOf(
            fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("실제 데이터"),
            fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("유저 PK"),
            fieldWithPath("content[].name").type(JsonFieldType.STRING).description("유저 이름"),
            fieldWithPath("content[].employeeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입"),
            fieldWithPath("content[].workTimeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무요일 타입"),
            fieldWithPath("content[].store").type(JsonFieldType.OBJECT).description("매장"),
            fieldWithPath("content[].store.id").type(JsonFieldType.NUMBER).description("매장 ID"),
            fieldWithPath("content[].store.name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("content[].store.type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),
            fieldWithPath("content[].store.deleted").type(JsonFieldType.BOOLEAN).description("매장 삭제여부"),

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
        val mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/scroll")
                    .queryParam("page", pageRequest.pageNumber.toString())
                    .queryParam("size", pageRequest.pageSize.toString())
                    .queryParam("userId", null)
                    .queryParam("keyword", null)
                    .queryParam("employeeType", null)
                    .queryParam("workTimeType", null)
                    .queryParam("storeId", null)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "get-users-scroll",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .summary("유저 스크롤 리스트 API")
                            .tag("user")
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
    @DisplayName("유저 상세 API 문서")
    fun detailTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockUser = User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1)
        val mockStore = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)
        val education = Education(1, Name("테스트 프로그램"), "테스트 내용", mutableListOf(EmployeeType.PART_TIME)).apply {
            addAddress(EducationPlace(1, this, Address.DUMMY, 3, 1, LocalDate.now(), TimeRange.DUMMY))
        }
        val educations = listOf(education)

        whenever(userService.detail(any())).thenReturn(Triple(mockUser, mockStore, educations))

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 PK"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("employeeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입"),
            fieldWithPath("workTimeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무 요일 타입"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("연락처"),

            fieldWithPath("store").type(JsonFieldType.OBJECT).description("매장 데이터"),
            fieldWithPath("store.id").type(JsonFieldType.NUMBER).description("매장 PK"),
            fieldWithPath("store.name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("store.type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),
            fieldWithPath("store.address").type(JsonFieldType.OBJECT).description("매장 주소"),
            fieldWithPath("store.address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("store.address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("store.address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("store.timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간"),
            fieldWithPath("store.timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간"),
            fieldWithPath("store.timeRange.endTime").type(JsonFieldType.STRING).description("마감시간"),
            fieldWithPath("store.deleted").type(JsonFieldType.BOOLEAN).description("매장 삭제여부"),

            fieldWithPath("educations").type(JsonFieldType.ARRAY).description("교육 프로그램 데이터"),
            fieldWithPath("educations[].id").type(JsonFieldType.NUMBER).description("교육 프로그램 PK"),
            fieldWithPath("educations[].name").type(JsonFieldType.STRING).description("교육 프로그램 이름"),

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
        val mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/{id}", 1)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "get-users-detail",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .summary("유저 상세 API")
                            .tag("user")
                            .responseFields(
                                responseFields
                            )
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("유저 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("유저 생성 API 문서")
    fun createTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockUser = User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1)
        val mockStore = Store(1, Name("메가커피 서대문역점"), StoreType.FRANCHISE, false)

        whenever(userService.create(any())).thenReturn(Pair(mockUser, mockStore))

        val createUserRequest = CreateUserRequest(
            email = "melon8372@gmail.com",
            name = "메로닝",
            address = AddressRequest("어느 도시", "어느 거리", "12345"),
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber.DUMMY.phone,
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 1
        )
        val jsonCreateUserRequest = ObjectMapperUtils.toPrettyJson(createUserRequest)

        val requestFields = listOf(
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("연락처"),
            fieldWithPath("employeeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입"),
            fieldWithPath("workTimeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무 요일 타입"),
            fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("유저 PK"),
        )

        val responseFields = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 PK"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),

            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소"),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),

            fieldWithPath("employeeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입"),
            fieldWithPath("workTimeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무 요일 타입"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("연락처"),

            fieldWithPath("store").type(JsonFieldType.OBJECT).description("매장 데이터"),
            fieldWithPath("store.id").type(JsonFieldType.NUMBER).description("매장 PK"),
            fieldWithPath("store.name").type(JsonFieldType.STRING).description("매장 이름"),
            fieldWithPath("store.type").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value))
                .description("매장 타입"),
            fieldWithPath("store.address").type(JsonFieldType.OBJECT).description("매장 주소"),
            fieldWithPath("store.address.city").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("store.address.street").type(JsonFieldType.STRING).description("거리"),
            fieldWithPath("store.address.zipCode").type(JsonFieldType.STRING).description("우편번호"),

            fieldWithPath("store.timeRange").type(JsonFieldType.OBJECT).description("매장 운영시간"),
            fieldWithPath("store.timeRange.startTime").type(JsonFieldType.STRING).description("오픈시간"),
            fieldWithPath("store.timeRange.endTime").type(JsonFieldType.STRING).description("마감시간"),

            fieldWithPath("store.deleted").type(JsonFieldType.BOOLEAN).description("매장 삭제여부"),

            fieldWithPath("createdAt").type(JsonFieldType.STRING).optional().description("생성일"),
            fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("변경일"),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonCreateUserRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "post-users",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .summary("유저 생성 API")
                            .tag("user")
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
    @DisplayName("유저 수정 API 문서")
    fun updateTest(contextProvider: RestDocumentationContextProvider) {
        // given
        val mockUser = User(1, Name("메로닝1"), EmployeeType.MANAGER, WorkTimeType.WEEKDAY, 1)

        whenever(userService.update(any(), any())).thenReturn(mockUser)

        val updateUserRequest = UpdateUserRequest(
            address = AddressRequest("어느 도시", "어느 거리", "12345"),
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber.DUMMY.phone,
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 1
        )
        val jsonUpdateUserRequest = ObjectMapperUtils.toPrettyJson(updateUserRequest)

        val requestFields = listOf(
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("주소").optional(),
            fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시").optional(),
            fieldWithPath("address.street").type(JsonFieldType.STRING).description("거리").optional(),
            fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호").optional(),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("연락처").optional(),
            fieldWithPath("employeeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value))
                .description("직원 타입").optional(),
            fieldWithPath("workTimeType").type(JsonFieldType.STRING)
                .attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value))
                .description("업무 요일 타입").optional(),
            fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("유저 PK").optional(),
        )

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.put("/api/v1/users/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonUpdateUserRequest)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "put-users",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .summary("유저 수정 API")
                            .tag("user")
                            .requestFields(requestFields)
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("유저 PK")
                    ),
                )
            )
    }

    @Test
    @DisplayName("유저 삭제 API 문서")
    fun deleteTest(contextProvider: RestDocumentationContextProvider) {
        // given
        doNothing().`when`(userService).delete(any())

        // when, then
        val mockMvc = MockMvcBuilders.standaloneSetup(userApiController)
            .setControllerAdvice(ExceptionHandler())
            .setConversionService(DefaultFormattingConversionService())
            .setMessageConverters(MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
            .build()

        mockMvc
            .perform(
                RestDocumentationRequestBuilders.delete("/api/v1/users/{id}", 1)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    "delete-users",
                    RestDocumentUtils.getDocumentRequest(),
                    RestDocumentUtils.getDocumentResponse(),
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .summary("유저 삭제 API")
                            .tag("user")
                            .build()
                    ),
                    pathParameters(
                        parameterWithName("id").description("유저 PK")
                    ),
                )
            )
    }
}
