package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.core.domain.user.usecase.RegisterEducationAddressFacadeService
import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import com.meloning.megaCoffee.domain.user.dto.CreateUserRequest
import com.meloning.megaCoffee.domain.user.dto.CreateUserResponse
import com.meloning.megaCoffee.domain.user.dto.RegisterEducationAddressRequest
import com.meloning.megaCoffee.domain.user.dto.ScrollUserRequest
import com.meloning.megaCoffee.domain.user.dto.ScrollUserResponse
import com.meloning.megaCoffee.domain.user.dto.UpdateUserRequest
import com.meloning.megaCoffee.domain.user.dto.UserDetailResponse
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
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserApiController(
    private val userService: UserService,
    private val registerEducationAddressFacadeService: RegisterEducationAddressFacadeService
) {

    @GetMapping("/users/scroll")
    fun scroll(
        request: ScrollUserRequest,
        @PageableDefault(page = 0, size = 8) pageable: Pageable
    ): ResponseEntity<Slice<ScrollUserResponse>> {
        val (content, hasNext) = userService.scroll(request.toCommand(), pageable.pageNumber, pageable.pageSize)
        return ResponseEntity.ok(SliceImpl(content.map { ScrollUserResponse.from(it.first, it.second) }, pageable, hasNext))
    }

    @GetMapping("/users/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<UserDetailResponse> {
        val (user, store, educations) = userService.detail(id)
        return ResponseEntity.ok(UserDetailResponse.from(user, store, educations))
    }

    @PostMapping("/users")
    fun create(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val (user, store) = userService.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/users/${user.id}"))
            .body(CreateUserResponse.from(user, store))
    }

    // path내 id로 로그인 유저 Id를 대체함
    @PostMapping("/users/{id}/education-place/register")
    fun register(
        @PathVariable id: Long,
        @Valid @RequestBody request: RegisterEducationAddressRequest
    ): ResponseEntity<Void> {
        registerEducationAddressFacadeService.execute(id, request.toCommand())
        return ResponseEntity.accepted().build()
    }

    @PutMapping("/users/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<Void> {
        userService.update(id, request.toCommand())
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
