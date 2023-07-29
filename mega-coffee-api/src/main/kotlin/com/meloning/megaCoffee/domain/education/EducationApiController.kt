package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationResponse
import com.meloning.megaCoffee.domain.education.dto.EducationDetailResponse
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationAddressesRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class EducationApiController(
    private val educationService: EducationService
) {

    @PostMapping("/educations")
    fun create(@Valid @RequestBody request: CreateEducationRequest): ResponseEntity<CreateEducationResponse> {
        val education = educationService.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/educations/${education.id}"))
            .body(CreateEducationResponse.from(education))
    }

    @GetMapping("/educations/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<EducationDetailResponse> {
        val education = educationService.detail(id)
        return ResponseEntity.ok(EducationDetailResponse.from(education))
    }

    @PostMapping("/edcuations/{id}/address/register")
    fun register(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterEducationAddressesRequest
    ): ResponseEntity<Void> {
        educationService.registerAddress(id, request.toCommand())
        return ResponseEntity.accepted().build()
    }
}
