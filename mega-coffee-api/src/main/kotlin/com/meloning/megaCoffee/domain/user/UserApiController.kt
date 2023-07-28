package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import org.springframework.data.domain.Pageable
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
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserApiController(
    private val userService: UserService
) {

    @GetMapping("/users/scroll")
    fun scroll(
        @PageableDefault(page = 0, size = 8) pageable: Pageable
    ): ResponseEntity<Void> {
        val (content, hasNext) = userService.scroll( , pageable.pageNumber, pageable.pageSize)
    }

    @GetMapping("/users/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<Void> {
        val (user, store, educations) = userService.detail(id)
    }

    @PostMapping("/users")
    fun create(@Valid @RequestBody ): ResponseEntity<Void> {

    }

    @PutMapping("/users/{id}")
    fun update(@Valid @RequestBody ): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
