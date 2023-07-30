package com.meloning.megaCoffee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventConsumerApplication

fun main(args: Array<String>) {
    runApplication<EventConsumerApplication>(*args)
}
