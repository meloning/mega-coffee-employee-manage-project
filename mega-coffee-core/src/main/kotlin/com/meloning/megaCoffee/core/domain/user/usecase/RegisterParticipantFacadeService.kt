package com.meloning.megaCoffee.core.domain.user.usecase

interface RegisterParticipantFacadeService {
    fun execute(id: Long, userId: Long, educationPlaceIds: List<Long>)
}
