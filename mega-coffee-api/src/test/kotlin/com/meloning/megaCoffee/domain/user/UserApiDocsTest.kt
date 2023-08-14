package com.meloning.megaCoffee.domain.user

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.meloning.megaCoffee.config.document.ApiRestDocsTest
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ApiRestDocsTest
class UserApiDocsTest {

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var userApiController: UserApiController

    @Test
    @DisplayName("유저 스크롤 리스트 API")
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
            RequestDocumentation.parameterWithName("page").attributes(Attributes.key("format").value("NUMBER")).description("페이지 번호").optional(),
            RequestDocumentation.parameterWithName("size").attributes(Attributes.key("format").value("NUMBER")).description("출력될 요소 개수").optional(),

            RequestDocumentation.parameterWithName("userId").attributes(Attributes.key("format").value("STRING")).description("마지막 유저 ID").optional(),
            RequestDocumentation.parameterWithName("keyword").attributes(Attributes.key("format").value("STRING")).description("유저 이름 검색").optional(),
            RequestDocumentation.parameterWithName("employeeType").attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value)).description("직원 타입").optional(),
            RequestDocumentation.parameterWithName("workTimeType").attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value)).description("업무요일 타입").optional(),
            RequestDocumentation.parameterWithName("storeId").attributes().description("매장 ID").optional()
        ).toTypedArray()

        val responseFields = listOf(
            PayloadDocumentation.fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("실제 데이터"),
            PayloadDocumentation.fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("유저 ID"),
            PayloadDocumentation.fieldWithPath("content[].name").type(JsonFieldType.STRING).description("유저 이름"),
            PayloadDocumentation.fieldWithPath("content[].employeeType").type(JsonFieldType.STRING).attributes(RestDocumentUtils.generatedEnumAttrs(EmployeeType::class.java, EmployeeType::value)).description("직원 타입"),
            PayloadDocumentation.fieldWithPath("content[].workTimeType").type(JsonFieldType.STRING).attributes(RestDocumentUtils.generatedEnumAttrs(WorkTimeType::class.java, WorkTimeType::value)).description("업무요일 타입"),
            PayloadDocumentation.fieldWithPath("content[].store").type(JsonFieldType.OBJECT).description("매장"),
            PayloadDocumentation.fieldWithPath("content[].store.id").type(JsonFieldType.NUMBER).description("매장 ID"),
            PayloadDocumentation.fieldWithPath("content[].store.name").type(JsonFieldType.STRING).description("매장 이름"),
            PayloadDocumentation.fieldWithPath("content[].store.type").type(JsonFieldType.STRING).attributes(RestDocumentUtils.generatedEnumAttrs(StoreType::class.java, StoreType::value)).description("매장 타입"),
            PayloadDocumentation.fieldWithPath("content[].store.deleted").type(JsonFieldType.BOOLEAN).description("매장 삭제여부"),

            PayloadDocumentation.fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("페이지 내 정렬 정보").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬설정이 비어있는지").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되어있지 않은지").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("오프셋 값").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 값").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("출력될 요소 개수").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 된건지").ignored(),
            PayloadDocumentation.fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 안된건지").ignored(),

            PayloadDocumentation.fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),

            PayloadDocumentation.fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬"),
            PayloadDocumentation.fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬설정이 비어있는지"),
            PayloadDocumentation.fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지"),
            PayloadDocumentation.fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되어있지 않은지"),

            PayloadDocumentation.fieldWithPath("size").type(JsonFieldType.NUMBER).description("몇개까지 보이게 할건지"),
            PayloadDocumentation.fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("처음인지"),
            PayloadDocumentation.fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막인지"),
            PayloadDocumentation.fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지에서 출력된 요소 개수"),
            PayloadDocumentation.fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는지"),
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
                    .param("page", pageRequest.pageNumber.toString())
                    .param("size", pageRequest.pageSize.toString())
                    .param("userId", null)
                    .param("keyword", null)
                    .param("employeeType", null)
                    .param("workTimeType", null)
                    .param("storeId", null)
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
}
