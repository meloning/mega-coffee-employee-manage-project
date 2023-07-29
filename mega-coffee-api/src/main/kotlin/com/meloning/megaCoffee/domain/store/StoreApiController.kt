package com.meloning.megaCoffee.domain.store

import com.meloning.megaCoffee.core.domain.store.usecase.StoreService
import com.meloning.megaCoffee.domain.store.dto.CreateStoreRequest
import com.meloning.megaCoffee.domain.store.dto.CreateStoreResponse
import com.meloning.megaCoffee.domain.store.dto.RegisterEducationsRequest
import com.meloning.megaCoffee.domain.store.dto.ScrollStoreResponse
import com.meloning.megaCoffee.domain.store.dto.StoreDetailResponse
import com.meloning.megaCoffee.domain.store.dto.UpdateStoreRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class StoreApiController(
    private val storeService: StoreService
) {

    @GetMapping("/stores/scroll")
    fun scroll(
        @RequestParam(required = false) storeId: Long?,
        @PageableDefault(page = 0, size = 8) pageable: Pageable
    ): ResponseEntity<Slice<ScrollStoreResponse>> {
        val (content, hasNext) = storeService.scroll(storeId, pageable.pageNumber, pageable.pageSize)
        return ResponseEntity.ok(SliceImpl(content.map { ScrollStoreResponse.from(it) }, pageable, hasNext))
    }

    @GetMapping("/stores/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<StoreDetailResponse> {
        val (store, educations) = storeService.detail(id)
        return ResponseEntity.ok(StoreDetailResponse.from(store, educations))
    }

    @PostMapping("/stores")
    fun create(@Valid @RequestBody request: CreateStoreRequest): ResponseEntity<CreateStoreResponse> {
        val store = storeService.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/stores/${store.id}"))
            .body(CreateStoreResponse.from(store))
    }

    @PostMapping("/stores/{id}/education/register")
    fun register(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterEducationsRequest
    ): ResponseEntity<Void> {
        storeService.registerForEducation(id, request.educations)
        return ResponseEntity.accepted().build()
    }

    @PutMapping("/stores/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStoreRequest
    ): ResponseEntity<Void> {
        storeService.update(id, request.toCommand())
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/stores/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        storeService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
