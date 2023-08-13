package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.user.dto.CreateUserRequest
import com.meloning.megaCoffee.domain.user.dto.RegisterEducationAddressRequest
import com.meloning.megaCoffee.domain.user.dto.ScrollUserRequest
import com.meloning.megaCoffee.domain.user.dto.UpdateUserRequest
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType

object UserSteps {

    fun 스크롤(
        userId: Long? = null,
        keyword: String? = null,
        employeeType: EmployeeType? = null,
        workTimeType: WorkTimeType? = null,
        storeId: Long? = null
    ) = ScrollUserRequest(userId, keyword, employeeType, workTimeType, storeId)

    fun 스크롤_요청(request: ScrollUserRequest, pageable: Pageable): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .queryParam("page", pageable.pageNumber)
            .queryParam("size", pageable.pageSize)
            .queryParam("userId", request.userId)
            .queryParam("keyword", request.keyword)
            .queryParam("employeeType", request.employeeType)
            .queryParam("workTimeType", request.workTimeType)
            .queryParam("storeId", request.storeId)
            .`when`()
            .get("/api/v1/users/scroll")
            .then()
            .log().all().extract()
    }

    fun 상세_요청(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", id)
            .`when`()
            .get("/api/v1/users/{id}")
            .then()
            .log().all().extract()
    }

    fun 생성_요청(request: CreateUserRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .`when`()
            .post("/api/v1/users")
            .then()
            .log().all().extract()
    }

    fun 생성(storeId: Long): CreateUserRequest = CreateUserRequest(
        email = "melon8372@gmail.com",
        name = "메로닝",
        address = AddressRequest("도시", "거리", "12345"),
        employeeType = EmployeeType.PART_TIME,
        phoneNumber = PhoneNumber.DUMMY.phone,
        workTimeType = WorkTimeType.WEEKEND,
        storeId = storeId
    )

    fun 교육장소_등록_요청(id: Long, request: RegisterEducationAddressRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .post("/api/v1/users/{id}/education-place/register")
            .then()
            .log().all().extract()
    }

    fun 교육장소_등록(): RegisterEducationAddressRequest = RegisterEducationAddressRequest(
        educationId = 1L,
        educationAddressIds = listOf(1L)
    )

    fun 수정_요청(id: Long, request: UpdateUserRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .put("/api/v1/users/{id}")
            .then()
            .log().all().extract()
    }

    fun 수정(): UpdateUserRequest = UpdateUserRequest(
        address = AddressRequest("어느도시", "어느 거리에서", "123"),
        employeeType = EmployeeType.OWNER,
        phoneNumber = PhoneNumber.DUMMY.phone,
        workTimeType = null,
        storeId = null
    )

    fun 삭제_요청(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", id)
            .`when`()
            .delete("/api/v1/users/{id}")
            .then()
            .log().all().extract()
    }
}
