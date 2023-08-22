package com.meloning.megaCoffee.util

import com.meloning.megaCoffee.common.constant.ZoneIds
import org.quartz.CronExpression
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import java.time.ZoneId
import java.util.*

object CronTriggerFactory {

    fun createCronTrigger(
        triggerKey: TriggerKey,
        cronExpression: CronExpression,
        description: String,
        zoneId: ZoneId = ZoneId.of(ZoneIds.UTC_STR)
    ): CronTrigger {
        return TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .withSchedule(
                CronScheduleBuilder.cronSchedule(cronExpression)
                    .inTimeZone(TimeZone.getTimeZone(zoneId))
                    .withMisfireHandlingInstructionIgnoreMisfires()
            )
            .withDescription(description)
            .build()
    }
}
