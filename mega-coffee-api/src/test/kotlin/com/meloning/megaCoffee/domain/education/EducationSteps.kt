package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationAddressParticipantRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationAddressesRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterStoresRequest
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.http.MediaType
import java.time.LocalDate
import java.time.LocalTime

object EducationSteps {

    fun 생성_요청(request: CreateEducationRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .`when`()
            .post("/api/v1/educations")
            .then()
            .log().all().extract()
    }

    fun 생성(): CreateEducationRequest = CreateEducationRequest(
        name = "테스트 교육",
        content = "테스트 교육 프로그램입니다.",
        targetTypes = listOf(EmployeeType.MANAGER, EmployeeType.PART_TIME)
    )

    fun 상세_요청(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", id)
            .`when`()
            .get("/api/v1/educations/{id}")
            .then()
            .log().all().extract()
    }

    fun 교육장소_생성_요청(id: Long, request: RegisterEducationAddressesRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .post("/api/v1/educations/{id}/address/register")
            .then()
            .log().all().extract()
    }

    fun 교육장소_생성(): RegisterEducationAddressesRequest = RegisterEducationAddressesRequest(
        addresses = listOf(
            RegisterEducationAddressesRequest.EducationAddressRequest(
                address = AddressRequest("어느 도시", "어느 거리", "12345"),
                maxParticipant = 2,
                date = LocalDate.now().toString(),
                timeRange = TimeRangeRequest(LocalTime.MIN.toString(), LocalTime.MAX.withNano(0).toString())
            )
        )
    )

    fun 매장_등록_요청(id: Long, request: RegisterStoresRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .post("/api/v1/educations/{id}/stores/register")
            .then()
            .log().all().extract()
    }

    fun 매장_등록(): RegisterStoresRequest = RegisterStoresRequest(listOf(1))

    fun 유저_교육장소_등록_요청(id: Long, userId: Long, request: RegisterEducationAddressParticipantRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .pathParam("userId", userId)
            .body(request)
            .`when`()
            .post("/api/v1/educations/{id}/addresses/participant/{userId}/register")
            .then()
            .log().all().extract()
    }

    fun 유저_교육장소_등록(): RegisterEducationAddressParticipantRequest = RegisterEducationAddressParticipantRequest(listOf(1))
}
