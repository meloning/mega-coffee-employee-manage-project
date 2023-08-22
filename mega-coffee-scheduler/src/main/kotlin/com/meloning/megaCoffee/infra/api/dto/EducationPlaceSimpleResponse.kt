package com.meloning.megaCoffee.infra.api.dto

import org.quartz.JobDataMap

data class EducationPlaceSimpleResponse(
    val id: Long,
    val educationName: String,
    val address: String,
    val date: String,
    val time: String,
    val maxParticipant: Int,
    val currentParticipant: Int,
    val createdAt: String?,
    val updatedAt: String?
) {

    fun toJobDataMap() = JobDataMap(
        mutableMapOf(
            "id" to id,
            "educationName" to educationName,
            "address" to address,
            "date" to date,
            "time" to time,
        )
    )
}
