package com.meloning.megaCoffee.domain.education

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.meloning.megaCoffee.common.util.ObjectMapperUtils
import com.meloning.megaCoffee.config.document.ApiRestDocsTest
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.error.ExceptionHandler
import com.meloning.megaCoffee.util.document.RestDocumentUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

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
}
