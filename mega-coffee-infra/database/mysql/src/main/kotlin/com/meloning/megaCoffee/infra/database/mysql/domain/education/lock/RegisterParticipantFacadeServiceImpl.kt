package com.meloning.megaCoffee.infra.database.mysql.domain.education.lock

import com.meloning.megaCoffee.common.error.exception.RetryFailedException
import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterParticipantFacadeService
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.RecoveryCallback
import org.springframework.retry.RetryCallback
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service

@Service
class RegisterParticipantFacadeServiceImpl(
    private val educationService: EducationService,
    private val retryTemplate: RetryTemplate
) : RegisterParticipantFacadeService {

    override fun execute(id: Long, userId: Long, educationAddressIds: List<Long>) {
        try {
            educationService.registerParticipant(id, userId, educationAddressIds)
        } catch (e: ObjectOptimisticLockingFailureException) {
            retryTemplate.execute(
                RetryCallback<Unit, Exception> {
                    educationService.registerParticipant(id, userId, educationAddressIds)
                },
                RecoveryCallback {
                    throw RetryFailedException("OptimisticLock 발생에 대한 Retry 처리를 실패하였습니다.", e)
                }
            )
        }
    }
}
