package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.domain.user.dto.ScrollUserRequest
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.data.domain.Pageable

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
}
