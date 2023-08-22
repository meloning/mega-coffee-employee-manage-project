package com.meloning.megaCoffee.core.job

import com.meloning.megaCoffee.common.constant.ZoneIds
import com.meloning.megaCoffee.core.api.EmployeeManageExternalApi
import com.meloning.megaCoffee.util.JobDetailFactory
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_JOB_GROUP
import com.meloning.megaCoffee.util.SchedulerConstants.EDUCATION_PLACE_TRIGGER_GROUP
import com.meloning.megaCoffee.util.SchedulerConstants.PARTICIPANT_NOTIFY_JOB
import com.meloning.megaCoffee.util.SchedulerConstants.PARTICIPANT_NOTIFY_JOB_DESCRIPTION
import com.meloning.megaCoffee.util.SchedulerConstants.PARTICIPANT_NOTIFY_TRIGGER
import com.meloning.megaCoffee.util.SchedulerConstants.PARTICIPANT_NOTIFY_TRIGGER_DESCRIPTION
import com.meloning.megaCoffee.util.SimpleTriggerFactory
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerKey
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId

@Component
class TodayEducationPlaceFetchAndGenerateJob(
    private val employeeManageApi: EmployeeManageExternalApi,
    private val scheduler: Scheduler
) : Job {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(context: JobExecutionContext) {
        val today = LocalDate.now(ZoneId.of(ZoneIds.ASIA_SEOUL))

        val response = employeeManageApi.getEducationPlacesByDate(today)
        logger.info("EducationPlace Size: ${response.size}")

        response.forEach {
            val jobDetail = JobDetailFactory.createJobDetail(
                jobClass = TodayEducationPlaceParticipantsNotifyJob::class.java,
                jobKey = JobKey("$PARTICIPANT_NOTIFY_JOB-${it.id}", EDUCATION_PLACE_JOB_GROUP),
                dataMap = it.toJobDataMap(),
                description = PARTICIPANT_NOTIFY_JOB_DESCRIPTION
            )

            val trigger = SimpleTriggerFactory.createSimpleTrigger(
                triggerKey = TriggerKey("$PARTICIPANT_NOTIFY_TRIGGER-${it.id}", EDUCATION_PLACE_TRIGGER_GROUP),
                startAt = today.atTime(7, 0),
                description = PARTICIPANT_NOTIFY_TRIGGER_DESCRIPTION
            )

            scheduler.scheduleJob(jobDetail, trigger)
        }
        logger.info("[quartzScheduler] ${context.jobDetail.key} run successfully.")
    }
}
