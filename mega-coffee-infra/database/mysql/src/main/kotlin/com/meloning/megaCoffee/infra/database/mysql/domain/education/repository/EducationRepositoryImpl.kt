package com.meloning.megaCoffee.infra.database.mysql.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationPlacesVO
import org.hibernate.Hibernate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate

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
            EducationEntity.from(education).apply {
                update(
                    EducationPlacesVO.from(education.educationPlaces)
                )
            }
        )
    }

    override fun findDetailById(id: Long): Education? {
        val educationEntity = educationJpaRepository.findByIdOrNull(id)
        educationEntity?.educationPlaces?.value?.forEach {
            Hibernate.initialize(it)
        }
        Hibernate.initialize(educationEntity?.targetTypes)
        return educationEntity?.toModel()?.apply {
            update(educationEntity.educationPlaces.toModel())
        }
    }

    override fun findById(id: Long): Education? {
        return educationJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun findAllByStoreId(storeId: Long): List<Education> {
        val educations = educationJpaRepository.findAllByStoreId(storeId)
        educations.forEach { educationEntity ->
            educationEntity.educationPlaces.value.forEach {
                Hibernate.initialize(it)
            }
            Hibernate.initialize(educationEntity.targetTypes)
        }
        return educations.map { it.toModel().apply { update(it.educationPlaces.toModel()) } }
    }

    override fun findAllByStoreIdAndUserId(storeId: Long, userId: Long): List<Education> {
        val (educations, userEducationPlaces) = educationJpaRepository.findAllByStoreIdAndUserId(storeId, userId)
        return educations.map { educationEntity ->
            educationEntity.toModel().apply {
                val result = educationEntity.educationPlaces.toModel().filterByContainedIds(userEducationPlaces.map { it.id!! })
                update(EducationPlaces(result.toMutableList()))
            }
        }
    }

    override fun findEducationPlaceAllByUserId(userId: Long): List<EducationPlace> {
        return educationJpaRepository.findEducationPlaceAllByUserId(userId).map { it.toModel() }
    }

    override fun findEducationPlaceAllByDate(date: LocalDate): List<EducationPlace> {
        return educationJpaRepository.findEducationPlaceAllByDate(date).map { it.toModel() }
    }

    override fun existsByName(name: Name): Boolean {
        return educationJpaRepository.existsByName(NameVO.from(name))
    }

    override fun deleteById(id: Long) {
        educationJpaRepository.deleteById(id)
    }
}
