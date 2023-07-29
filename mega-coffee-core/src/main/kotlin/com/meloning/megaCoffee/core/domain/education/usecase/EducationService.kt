package com.meloning.megaCoffee.core.domain.education.usecase

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.repository.findByIdOrThrow
import com.meloning.megaCoffee.core.domain.education.repository.findDetailByIdOrThrow
import com.meloning.megaCoffee.core.domain.education.usecase.command.CreateEducationCommand
import com.meloning.megaCoffee.core.domain.education.usecase.command.RegisterEducationAddressesCommand
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EducationService(
    private val educationRepository: IEducationRepository
) {

    @Transactional(readOnly = true)
    fun detail(id: Long): Education {
        return educationRepository.findDetailByIdOrThrow(id)
    }

    fun create(command: CreateEducationCommand): Education {
        if (educationRepository.existsByName(command.name)) {
            throw AlreadyExistException("이미 존재하는 교육프로그램입니다.")
        }

        return educationRepository.save(command.toModel())
    }

    fun registerAddress(id: Long, command: RegisterEducationAddressesCommand) {
        val education = educationRepository.findByIdOrThrow(id)
        education.update(command.toModel(education))

        educationRepository.update(education)
    }
}
