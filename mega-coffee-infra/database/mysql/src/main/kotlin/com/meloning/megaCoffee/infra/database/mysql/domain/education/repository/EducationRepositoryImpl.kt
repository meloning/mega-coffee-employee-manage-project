package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class EducationRepositoryImpl(
    private val educationJpaRepository: EducationJpaRepository
) : IEducationRepository {
    override fun save(education: Education): Education {
        return educationJpaRepository.save(EducationEntity.from(education)).toModel()
    }

    override fun saveAll(educations: List<Education>): List<Education> {
        return educationJpaRepository.saveAll(educations.map { EducationEntity.from(it) }).map { it.toModel() }
    }

    override fun update(education: Education) {
        educationJpaRepository.save(
            EducationEntity.from(education)
        )
    }

    override fun findById(id: Long): Education? {
        return educationJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun existsByName(name: Name): Boolean {
        return educationJpaRepository.existsByName(NameVO.from(name))
    }

    override fun deleteById(id: Long) {
        educationJpaRepository.deleteById(id)
    }
}
