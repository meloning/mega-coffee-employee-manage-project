package com.meloning.megaCoffee.domain.store.dto

import javax.validation.constraints.Size

data class RegisterEducationsRequest(
    @field:Size(min = 1)
    val educations: List<Long>
)
