package com.meloning.megaCoffee.domain.store

import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.domain.common.dto.AddressRequest
import com.meloning.megaCoffee.domain.common.dto.TimeRangeRequest
import com.meloning.megaCoffee.domain.store.dto.CreateStoreRequest
import com.meloning.megaCoffee.domain.store.dto.UpdateStoreRequest
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import java.time.LocalTime

object StoreSteps {

    fun 스크롤_요청(storeId: Long? = null, pageable: Pageable): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .queryParam("page", pageable.pageNumber)
            .queryParam("size", pageable.pageSize)
            .queryParam("storeId", storeId)
            .`when`()
            .get("/api/v1/stores/scroll")
            .then()
            .log().all().extract()
    }

    fun 상세_요청(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", id)
            .`when`()
            .get("/api/v1/stores/{id}")
            .then()
            .log().all().extract()
    }

    fun 생성_요청(request: CreateStoreRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .`when`()
            .post("/api/v1/stores")
            .then()
            .log().all().extract()
    }

    fun 생성(): CreateStoreRequest = CreateStoreRequest(
        name = "메가커피 강남점",
        type = StoreType.FRANCHISE,
        address = AddressRequest("어느 도시", "어느 거리", "123"),
        timeRange = TimeRangeRequest(LocalTime.MIN.toString(), LocalTime.MAX.withNano(0).toString())
    )

    fun 수정_요청(id: Long, request: UpdateStoreRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .body(request)
            .`when`()
            .put("/api/v1/stores/{id}")
            .then()
            .log().all().extract()
    }

    fun 수정(ownerId: Long? = null): UpdateStoreRequest = UpdateStoreRequest(
        type = StoreType.COMPANY_OWNED,
        ownerId = ownerId,
        address = AddressRequest("어떤 도시", "어떤 거리", "123456"),
        timeRange = TimeRangeRequest(LocalTime.MIN.toString(), LocalTime.MAX.withNano(0).toString())
    )

    fun 삭제_요청(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .pathParam("id", id)
            .`when`()
            .delete("/api/v1/stores/{id}")
            .then()
            .log().all().extract()
    }
}
