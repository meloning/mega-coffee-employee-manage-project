package com.meloning.megaCoffee.util

import org.quartz.SimpleTrigger
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.TriggerKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object SimpleTriggerFactory {

    fun createSimpleTrigger(
        triggerKey: TriggerKey,
        startAt: LocalDateTime,
        description: String
    ): SimpleTrigger =
        newTrigger()
            .withIdentity(triggerKey)
            .startAt(Date.from(startAt.toInstant(ZoneOffset.ofHours(9))))
            .withDescription(description)
            .build() as SimpleTrigger
}
