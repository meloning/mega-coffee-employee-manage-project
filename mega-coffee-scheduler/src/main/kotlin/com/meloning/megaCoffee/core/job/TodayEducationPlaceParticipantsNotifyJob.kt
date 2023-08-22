package com.meloning.megaCoffee.core.job

import com.meloning.megaCoffee.core.api.EmployeeManageExternalApi
import com.meloning.megaCoffee.core.infra.event.EventSender
import com.meloning.megaCoffee.core.infra.event.EventType
import com.meloning.megaCoffee.core.model.EducationPlace
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TodayEducationPlaceParticipantsNotifyJob(
    private val employeeManageApi: EmployeeManageExternalApi,
    private val eventSender: EventSender
) : Job {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(context: JobExecutionContext) {
        val educationPlace = EducationPlace.from(context.jobDetail.jobDataMap)

        val response = employeeManageApi.getParticipantsByEducationPlace(educationPlace.id)
        logger.info("Participant Size: ${response.size}")

        response.forEach { user ->
            eventSender.send(
                type = EventType.EMAIL,
                payload = mapOf(
                    "email" to user.email,
                    "username" to user.name,
                    "educationName" to educationPlace.educationName,
                    "educationPlaceAddress" to educationPlace.address,
                    "date" to educationPlace.date,
                    "time" to educationPlace.time,
                    "type" to "complete_user_education"
                )
            )
        }

        logger.info("[quartzScheduler] ${context.jobDetail.key} run successfully.")
    }
}
