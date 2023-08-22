package com.meloning.megaCoffee.infra.api

import com.meloning.megaCoffee.config.RestTemplateConfig
import com.meloning.megaCoffee.core.api.EmployeeManageExternalApi
import com.meloning.megaCoffee.infra.api.dto.EducationPlaceSimpleResponse
import com.meloning.megaCoffee.infra.api.dto.ParticipantResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

@Component
class EmployeeManageApi(
    private val restTemplate: RestTemplate
) : EmployeeManageExternalApi {

    private fun defaultHttpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
    }

    override fun getEducationPlacesByDate(date: LocalDate): List<EducationPlaceSimpleResponse> {
        val uri = UriComponentsBuilder.fromUriString(RestTemplateConfig.EMPLOYEE_MANAGE_API_URL)
            .path("/api/v1/educations/places")
            .queryParam("date", date)
            .toUriString()

        val responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, object : ParameterizedTypeReference<List<EducationPlaceSimpleResponse>>() {})

        return responseEntity.body!!
    }

    override fun getParticipantsByEducationPlace(educationPlaceId: Long): List<ParticipantResponse> {
        val uri = UriComponentsBuilder.fromUriString(RestTemplateConfig.EMPLOYEE_MANAGE_API_URL)
            .path("/api/v1/educations/places/$educationPlaceId/participants")
            .toUriString()
        val responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, object : ParameterizedTypeReference<List<ParticipantResponse>>() {})

        return responseEntity.body!!
    }
}
