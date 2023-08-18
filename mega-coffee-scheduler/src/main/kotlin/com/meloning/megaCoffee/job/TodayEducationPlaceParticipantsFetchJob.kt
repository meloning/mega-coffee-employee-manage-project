package com.meloning.megaCoffee.job

import com.meloning.megaCoffee.infra.api.EmployeeManageApi
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TodayEducationPlaceParticipantsFetchJob(
    private val employeeManageApi: EmployeeManageApi
) : Job {
    override fun execute(context: JobExecutionContext) {
        val today = LocalDate.now()
    }
}
