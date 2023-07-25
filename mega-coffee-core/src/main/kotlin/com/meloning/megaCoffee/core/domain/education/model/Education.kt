package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import java.time.Instant

data class Education(
    var id: Long? = null,
    val name: Name,
    var content: String,
    var targetTypes: MutableList<EmployeeType> = mutableListOf(),
    // 교육장소들
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Education

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
