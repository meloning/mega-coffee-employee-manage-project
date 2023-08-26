package com.meloning.megaCoffee.config.document

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.restdocs.RestDocumentationExtension
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@ExtendWith(
    value = [
        MockitoExtension::class,
        RestDocumentationExtension::class
    ]
)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
annotation class ApiRestDocsTest
