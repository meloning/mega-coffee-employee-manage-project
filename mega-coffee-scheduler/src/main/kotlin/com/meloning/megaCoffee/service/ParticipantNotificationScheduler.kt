package com.meloning.megaCoffee.service

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
class ParticipantNotificationScheduler(
    private val scheduler: Scheduler
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    private fun registerScheduler() {
        if (!scheduler.checkExists(TriggerKey.triggerKey("test", "testGroup"))) {
            val jobDetail = JobBuilder.newJob() // Job Class 명시
                .withIdentity("")
                .withDescription("")
                .storeDurably(true)
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity("")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("")
                        .inTimeZone(TimeZone.getDefault())
                        .withMisfireHandlingInstructionIgnoreMisfires()
                )
                .withDescription("")
                .build()

            scheduler.scheduleJob(jobDetail, trigger)
        }
    }

    companion object {
    }
}
