package com.meloning.megaCoffee.core.service

import com.meloning.megaCoffee.common.constant.ZoneIds
import com.meloning.megaCoffee.core.job.TodayEducationPlaceFetchAndGenerateJob
import com.meloning.megaCoffee.util.CronTriggerFactory
import com.meloning.megaCoffee.util.JobDetailFactory
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_JOB_GROUP
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_NOTIFY_GENERATE_JOB
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_NOTIFY_GENERATE_JOB_DESCRIPTION
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER_DESCRIPTION
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_TRIGGER_GROUP
import com.meloning.megaCoffee.util.SchedulerConstants.EVERY_DAY_2_AM_CRON_EXPRESSION
import org.quartz.CronExpression
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerKey
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZoneId
import javax.annotation.PostConstruct

@Service
class ParticipantNotificationScheduler(
    private val scheduler: Scheduler
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    private fun registerScheduler() {
        val educationPlaceNotifyGenerateTriggerKey = TriggerKey(EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER, EDUCATION_PLACE_TRIGGER_GROUP)

        if (!scheduler.checkExists(educationPlaceNotifyGenerateTriggerKey)) {
            val jobDetail = JobDetailFactory.createJobDetail(
                jobClass = TodayEducationPlaceFetchAndGenerateJob::class.java,
                jobKey = JobKey(EDUCATION_PLACE_NOTIFY_GENERATE_JOB, EDUCATION_PLACE_JOB_GROUP),
                description = EDUCATION_PLACE_NOTIFY_GENERATE_JOB_DESCRIPTION
            )

            val trigger = CronTriggerFactory.createCronTrigger(
                triggerKey = educationPlaceNotifyGenerateTriggerKey,
                cronExpression = CronExpression(EVERY_DAY_2_AM_CRON_EXPRESSION),
                description = EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER_DESCRIPTION,
                zoneId = ZoneId.of(ZoneIds.ASIA_SEOUL)
            )

            val nextFireTime = scheduler.scheduleJob(jobDetail, trigger)
            logger.info("Register Trigger : ${trigger.jobKey}, Next Fire Time: $nextFireTime")
        }
    }
}
