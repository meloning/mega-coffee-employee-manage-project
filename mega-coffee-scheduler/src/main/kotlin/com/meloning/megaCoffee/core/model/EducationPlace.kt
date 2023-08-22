package com.meloning.megaCoffee.core.model

import org.quartz.JobDataMap

data class EducationPlace(
    val id: Long,
    val educationName: String,
    val address: String,
    val date: String,
    val time: String
) {
    companion object {
        @JvmStatic
        fun from(jobDataMap: JobDataMap): EducationPlace =
            EducationPlace(
                id = jobDataMap["id"]!!.toString().toLong(),
                educationName = jobDataMap["educationName"]!!.toString(),
                address = jobDataMap["address"]!!.toString(),
                date = jobDataMap["date"]!!.toString(),
                time = jobDataMap["time"]!!.toString()
            )
    }
}
