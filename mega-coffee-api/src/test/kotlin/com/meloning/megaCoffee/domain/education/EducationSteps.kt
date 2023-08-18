package com.meloning.megaCoffee.domain.education

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import com.meloning.megaCoffee.domain.education.dto.CreateEducationRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlaceParticipantRequest
import com.meloning.megaCoffee.domain.education.dto.RegisterEducationPlacesRequest
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

    fun 교육장소_생성_요청(id: Long, request: RegisterEducationPlacesRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .post("/api/v1/educations/{id}/place/register")
            .then()
            .log().all().extract()
    }

    fun 교육장소_생성(): RegisterEducationPlacesRequest = RegisterEducationPlacesRequest(
        places = listOf(
            RegisterEducationPlacesRequest.EducationPlaceRequest(
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

    fun 매장_등록(storeId: Long = 1): RegisterStoresRequest = RegisterStoresRequest(listOf(storeId))

    fun 유저_교육장소_등록_요청(id: Long, userId: Long, request: RegisterEducationPlaceParticipantRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .pathParam("userId", userId)
            .body(request)
            .`when`()
            .post("/api/v1/educations/{id}/places/participant/{userId}/register")
            .then()
            .log().all().extract()
    }

    fun 유저_교육장소_등록(): RegisterEducationPlaceParticipantRequest = RegisterEducationPlaceParticipantRequest(listOf(1))

    fun 특정날짜의_교육장소_리스트_요청(date: LocalDate): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .param("date", date.toString())
            .`when`()
            .get("/api/v1/educations/places")
            .then()
            .log().all().extract()
    }

    fun 교육장소의_현재_참여자_리스트_요청(educationPlaceId: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", educationPlaceId)
            .`when`()
            .get("/api/v1/educations/places/{id}/participants")
            .then()
            .log().all().extract()
    }
}
