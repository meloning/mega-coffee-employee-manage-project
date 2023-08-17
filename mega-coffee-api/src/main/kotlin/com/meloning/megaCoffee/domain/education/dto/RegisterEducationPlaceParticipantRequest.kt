package com.meloning.megaCoffee.domain.education.dto

import javax.validation.constraints.Size

data class RegisterEducationPlaceParticipantRequest(
    @field:Size(min = 1, message = "최소 1개 이상이어야 합니다.")
    val educationPlaceIds: List<Long>
)
