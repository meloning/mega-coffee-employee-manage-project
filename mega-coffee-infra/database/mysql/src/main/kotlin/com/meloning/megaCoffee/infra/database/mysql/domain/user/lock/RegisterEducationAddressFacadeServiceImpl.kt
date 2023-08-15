package com.meloning.megaCoffee.infra.database.mysql.domain.user.lock

import com.meloning.megaCoffee.common.error.exception.RetryFailedException
import com.meloning.megaCoffee.core.domain.user.usecase.RegisterEducationAddressFacadeService
import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import com.meloning.megaCoffee.core.domain.user.usecase.command.RegisterEducationAddressCommand
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.RecoveryCallback
import org.springframework.retry.RetryCallback
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service

@Service
class RegisterEducationAddressFacadeServiceImpl(
    private val userService: UserService,
    private val retryTemplate: RetryTemplate
) : RegisterEducationAddressFacadeService {

    override fun execute(id: Long, command: RegisterEducationAddressCommand) {
        try {
            userService.registerEducationAddress(id, command)
        } catch (e: ObjectOptimisticLockingFailureException) {
            retryTemplate.execute(
                RetryCallback<Unit, Exception> {
                    userService.registerEducationAddress(id, command)
                },
                RecoveryCallback {
                    throw RetryFailedException("OptimisticLock 발생에 대한 Retry 처리를 실패하였습니다.", e)
                }
            )
        }
    }
}
