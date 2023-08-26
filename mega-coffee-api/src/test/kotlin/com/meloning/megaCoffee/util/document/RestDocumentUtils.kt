package com.meloning.megaCoffee.util.document

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.snippet.Attributes

object RestDocumentUtils {
    fun getDocumentRequest(): OperationRequestPreprocessor {
        return Preprocessors.preprocessRequest(
            Preprocessors.modifyUris()
                .scheme("https")
                .host("meloning-api.meloning.com")
                .removePort(),
            Preprocessors.prettyPrint()
        )
    }

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    }

    /**
     * 주어진 Enum 클래스의 각 항목에 대해 주어진 함수를 적용한 결과를 포함하는 문자열을 생성하고,
     * 이를 Attributes.Attribute 형식으로 반환합니다.
     *
     * @param enumClass Enum 클래스 타입
     * @param func 각 Enum 항목에 적용할 함수
     * @return 생성된 Attributes.Attribute 객체
     */
    fun <E : Enum<E>> generatedEnumAttrs(enumClass: Class<E>, func: (E) -> String): Attributes.Attribute {
        // Enum 상수들을 스트림으로 변환한 후, 각 항목에 함수를 적용하고 문자열로 변환하여 리스트로 수집합니다.
        val value = enumClass.enumConstants
            .joinToString("\n") { el -> "${el.name}(${func(el)})" }

        return Attributes.key("format").value(value)
    }
}
