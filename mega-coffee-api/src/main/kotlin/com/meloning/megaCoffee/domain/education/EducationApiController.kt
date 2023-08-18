package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationResponse
import com.meloning.megaCoffee.domain.education.dto.EducationDetailResponse
import com.meloning.megaCoffee.domain.education.dto.EducationPlaceSimpleResponse
import com.meloning.megaCoffee.domain.education.dto.ParticipantResponse
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlaceParticipantRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlacesRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterStoresRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class EducationApiController(
    private val educationService: EducationService,
    private val registerParticipantFacadeService: RegisterParticipantFacadeService
) {

    @GetMapping("/educations/places")
    fun getEducationPlaces(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<List<EducationPlaceSimpleResponse>> {
        val educationPlaces = educationService.getEducationPlace(date)
        return ResponseEntity.ok(educationPlaces.map { EducationPlaceSimpleResponse.from(it) })
    }

    @GetMapping("/educations/places/{id}/participants")
    fun getParticipants(
        @PathVariable id: Long
    ): ResponseEntity<List<ParticipantResponse>> {
        val participants = educationService.getParticipantsByPlaceId(id)
        return ResponseEntity.ok(participants.map { ParticipantResponse.from(it) })
    }

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

    @PostMapping("/educations/{id}/place/register")
    fun register(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterEducationPlacesRequest
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

    @PostMapping("/educations/{id}/places/participant/{userId}/register")
    fun registerParticipant(
        @PathVariable id: Long,
        @PathVariable userId: Long,
        @Valid @RequestBody request: RegisterEducationPlaceParticipantRequest
    ): ResponseEntity<Void> {
        registerParticipantFacadeService.execute(id, userId, request.educationPlaceIds)
        return ResponseEntity.accepted().build()
    }
}
