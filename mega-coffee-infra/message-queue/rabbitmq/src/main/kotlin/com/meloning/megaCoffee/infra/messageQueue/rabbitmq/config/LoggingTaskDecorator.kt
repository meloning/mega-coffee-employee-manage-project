package com.meloning.megaCoffee.infra.messageQueue.rabbitmq.config

import org.slf4j.MDC
import org.springframework.core.task.TaskDecorator

class LoggingTaskDecorator : TaskDecorator {

    override fun decorate(runnable: Runnable): Runnable {
        val threadContext = MDC.getCopyOfContextMap()
        return Runnable {
            threadContext?.let {
                MDC.setContextMap(it)
            }
            runnable.run()
        }
    }
}
