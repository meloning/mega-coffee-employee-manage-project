package com.meloning.megaCoffee.core.infra.event

enum class EventType(val value: String) {
    EMAIL("email.notification"),
    SLACK("slack.message");
}
