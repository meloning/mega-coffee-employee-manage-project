package com.meloning.megaCoffee.core.domain.user.model

enum class WorkTimeType(val value: String) {
    WEEKDAY("평일"),
    WEEKEND("주말");

    companion object {
        private val map = values().associateBy(WorkTimeType::value)

        @JvmStatic
        fun from(value: String): WorkTimeType = map[value] ?: WEEKDAY
    }
}
