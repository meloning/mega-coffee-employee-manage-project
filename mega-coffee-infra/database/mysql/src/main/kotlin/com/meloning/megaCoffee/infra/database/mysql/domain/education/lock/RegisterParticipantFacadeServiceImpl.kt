package com.meloning.megaCoffee.infra.database.mysql.domain.education.lock

import com.meloning.megaCoffee.common.error.exception.RetryFailedException
import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import org.hibernate.StaleStateException
import org.slf4j.LoggerFactory
import org.springframework.retry.RecoveryCallback
import org.springframework.retry.RetryCallback
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service

@Service
class RegisterParticipantFacadeServiceImpl(
    private val educationService: EducationService,
    private val retryTemplate: RetryTemplate
) : RegisterParticipantFacadeService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(id: Long, userId: Long, educationPlaceIds: List<Long>) {
        try {
            educationService.registerParticipant(id, userId, educationPlaceIds)
        } catch (e: StaleStateException) {
            retryTemplate.execute(
                RetryCallback<Unit, Exception> {
                    logger.warn("Retry registerParticipant caused By ${e.message}")
                    educationService.registerParticipant(id, userId, educationPlaceIds)
                },
                RecoveryCallback {
                    logger.error("Failed retry registerParticipant throwing RetryFailedException")
                    throw RetryFailedException("OptimisticLock 발생에 대한 Retry 처리를 실패하였습니다.", e)
                }
            )
        }
    }
}
