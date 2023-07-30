package com.meloning.megaCoffee.core.event

interface EventSender {
    fun send(type: EventType, payload: Map<String, String>)
}
