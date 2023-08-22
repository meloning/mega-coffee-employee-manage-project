package com.meloning.megaCoffee.core.infra.event

interface EventSender {
    fun send(type: EventType, payload: Map<String, String>)
}
