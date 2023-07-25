package com.meloning.megaCoffee.core.domain.education.repository

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education

interface IEducationRepository {
    fun save(education: Education): Education
    fun saveAll(educations: List<Education>): List<Education>
    fun update(education: Education)

    fun findById(id: Long): Education?

    fun existsByName(name: Name): Boolean

    fun deleteById(id: Long)
}
