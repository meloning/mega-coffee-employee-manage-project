package com.meloning.megaCoffee.core.domain.user.usecase.command

data class RegisterEducationAddressCommand(
    val educationId: Long,
    val educationAddressIds: List<Long>,
)
