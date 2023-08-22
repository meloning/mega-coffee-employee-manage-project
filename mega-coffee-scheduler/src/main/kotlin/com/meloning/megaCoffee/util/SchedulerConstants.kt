package com.meloning.megaCoffee.util

object SchedulerConstants {
    const val PARTICIPANT_NOTIFY_TRIGGER = "participant-notify-trigger"
    const val PARTICIPANT_NOTIFY_TRIGGER_DESCRIPTION = "참여자에게 교육장소 알림을 보내주기 위한 템플릿 Trigger"

    const val EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER = "education-place-notify-generate-trigger"
    const val EDUCATION_PLACE_NOTIFY_GENERATE_TRIGGER_DESCRIPTION = "교육 장소에 대한 알림 트리거를 생성하는 트리거"
    const val EDUCATION_PLACE_TRIGGER_GROUP = "education-place-trigger-group"

    const val PARTICIPANT_NOTIFY_JOB = "participant-notify-job"
    const val PARTICIPANT_NOTIFY_JOB_DESCRIPTION = "참여자에게 교육장소 알림을 보내주기 위한 Job"

    const val EDUCATION_PLACE_NOTIFY_GENERATE_JOB = "education-place-notify-generate-job"
    const val EDUCATION_PLACE_NOTIFY_GENERATE_JOB_DESCRIPTION = "교육 장소에 대한 알림 Job을 생성하는 Job"
    const val EDUCATION_PLACE_JOB_GROUP = "education-place-job-group"

    const val EVERY_DAY_2_AM_CRON_EXPRESSION = "0 0 2 * * ?"
}
