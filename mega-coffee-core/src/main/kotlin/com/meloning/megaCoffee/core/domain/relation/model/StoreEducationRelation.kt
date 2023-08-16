package com.meloning.megaCoffee.core.domain.relation.model

import com.meloning.megaCoffee.core.domain.education.model.Education
import java.time.Instant

data class StoreEducationRelation(
    val id: Long? = null,
    val storeId: Long,
    val education: Education,
    var createdAt: Instant? = null
) {

    companion object {
        @JvmStatic
        fun create(storeId: Long, education: Education) =
            StoreEducationRelation(storeId = storeId, education = education)
    }
}
