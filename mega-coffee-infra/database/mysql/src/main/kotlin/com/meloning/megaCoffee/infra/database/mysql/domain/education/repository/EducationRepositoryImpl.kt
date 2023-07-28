package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import org.hibernate.Hibernate
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

    override fun findDetailById(id: Long): Education? {
        val educationEntity = educationJpaRepository.findByIdOrNull(id)
        educationEntity?.educationAddresses?.value?.forEach {
            Hibernate.initialize(it)
        }
        return educationEntity?.toModel()?.apply {
            update(educationEntity.educationAddresses.toModel())
        }
    }

    override fun findById(id: Long): Education? {
        return educationJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun findAllByStoreId(storeId: Long): List<Education> {
        val educations = educationJpaRepository.findAllByStoreId(storeId)
        educations.forEach { educationEntity ->
            educationEntity.educationAddresses.value.forEach {
                Hibernate.initialize(it)
            }
        }
        return educations.map { it.toModel() }
    }

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<Education> {
        return educationJpaRepository.findAllByStoreIdAndUserId(storeId, userId).map { it.toModel() }
    }

    override fun existsByName(name: Name): Boolean {
        return educationJpaRepository.existsByName(NameVO.from(name))
    }

    override fun deleteById(id: Long) {
        educationJpaRepository.deleteById(id)
    }

    override fun countByEducationAddressId(educationAddressId: Long): Int {
        return educationJpaRepository.countByEducationAddressId(educationAddressId)
    }
}
