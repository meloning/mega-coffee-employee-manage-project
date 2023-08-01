package com.meloning.megaCoffee.infra.messageQueue.rabbitmq.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableAsync(proxyTargetClass = true)
class AsyncConfig : AsyncConfigurer {

    @Bean(name = ["asyncTaskExecutor"])
    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = Runtime.getRuntime().availableProcessors()
        executor.maxPoolSize = Runtime.getRuntime().availableProcessors() * 2
        executor.queueCapacity = 20
        executor.setTaskDecorator(LoggingTaskDecorator())
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        executor.setThreadNamePrefix("NOTIFICATION_")
        executor.setWaitForTasksToCompleteOnShutdown(true)
        return executor
    }
}
