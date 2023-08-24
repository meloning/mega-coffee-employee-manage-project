package com.meloning.megaCoffee.domain.store

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.meloning.megaCoffee.config.document.ApiRestDocsTest
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.usecase.StoreService
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

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
}
