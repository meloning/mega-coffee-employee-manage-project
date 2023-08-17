package com.meloning.megaCoffee.infra.database.mysql.domain.education.entity

import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces
import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces.Companion.MAX_EDUCATION_PLACE_COUNT
import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.OneToMany

@Embeddable
class EducationPlacesVO(
    @OneToMany(mappedBy = "education", cascade = [CascadeType.ALL], orphanRemoval = true)
    val value: MutableList<EducationPlaceEntity>
) {

    init {
        require(value.size <= MAX_EDUCATION_PLACE_COUNT) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 가능합니다." }
    }

    fun toModel(): EducationPlaces = EducationPlaces(value.map { it.toModel() }.toMutableList())

    companion object {
        @JvmStatic
        fun from(model: EducationPlaces): EducationPlacesVO = with(model) {
            EducationPlacesVO(value.map { EducationPlaceEntity.from(it) }.toMutableList())
        }
    }
}
