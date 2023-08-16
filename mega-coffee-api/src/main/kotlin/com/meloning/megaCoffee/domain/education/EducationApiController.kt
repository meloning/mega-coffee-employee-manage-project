package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationResponse
import com.meloning.megaCoffee.domain.education.dto.EducationDetailResponse
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationAddressParticipantRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationAddressesRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterStoresRequest
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
    private val educationService: EducationService,
    private val registerParticipantFacadeService: RegisterParticipantFacadeService
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

    @PostMapping("/educations/{id}/address/register")
    fun register(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterEducationAddressesRequest
    ): ResponseEntity<Void> {
        educationService.registerAddress(id, request.toCommand())
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/educations/{id}/stores/register")
    fun registerStores(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterStoresRequest
    ): ResponseEntity<Void> {
        educationService.registerStore(id, request.stores)
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/educations/{id}/addresses/participant/{userId}/register")
    fun registerParticipant(
        @PathVariable id: Long,
        @PathVariable userId: Long,
        @Valid @RequestBody request: RegisterEducationAddressParticipantRequest
    ): ResponseEntity<Void> {
        registerParticipantFacadeService.execute(id, userId, request.educationAddressIds)
        return ResponseEntity.accepted().build()
    }
}
