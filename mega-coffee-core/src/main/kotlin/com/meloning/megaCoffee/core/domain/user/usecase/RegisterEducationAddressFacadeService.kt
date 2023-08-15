package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.core.domain.user.usecase.command.RegisterEducationAddressCommand

interface RegisterEducationAddressFacadeService {
    fun execute(id: Long, command: RegisterEducationAddressCommand)
}
