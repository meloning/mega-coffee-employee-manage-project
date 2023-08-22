package com.meloning.megaCoffee.util

import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobKey

object JobDetailFactory {

    fun createJobDetail(
        jobClass: Class<out Job>,
        jobKey: JobKey,
        dataMap: JobDataMap,
        description: String
    ): JobDetail {
        return JobBuilder.newJob(jobClass)
            .withIdentity(jobKey)
            .withDescription(description)
            .usingJobData(dataMap)
            .storeDurably(true) // Fix error: Jobs added with no trigger must be durable.
            .build()
    }

    fun createJobDetail(
        jobClass: Class<out Job>,
        jobKey: JobKey,
        description: String
    ): JobDetail {
        return JobBuilder.newJob(jobClass)
            .withIdentity(jobKey)
            .withDescription(description)
            .storeDurably(true) // Fix error: Jobs added with no trigger must be durable.
            .build()
    }
}
