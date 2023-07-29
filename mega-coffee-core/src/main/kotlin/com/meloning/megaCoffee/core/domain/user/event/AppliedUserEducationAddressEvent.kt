package com.meloning.megaCoffee.core.domain.user.event

data class AppliedUserEducationAddressEvent(
    val username: String,
    val educationName: String,
    val educationAddress: String,
    val date: String,
    val time: String
)
