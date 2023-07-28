package com.meloning.megaCoffee.infra.database.mysql.domain.education.entity

import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses.Companion.MAX_EDUCATION_PLACE_COUNT
import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.OneToMany

@Embeddable
class EducationAddressesVO(
    @OneToMany(mappedBy = "education", cascade = [CascadeType.PERSIST, CascadeType.REMOVE], orphanRemoval = true)
    val value: MutableList<EducationAddressEntity>
) {

    init {
        require(value.isNotEmpty()) { "교육 장소는 반드시 1개 이상이어야 합니다." }
        require(value.size <= MAX_EDUCATION_PLACE_COUNT) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 가능합니다." }
    }

    fun toModel(): EducationAddresses = EducationAddresses(value.map { it.toModel() }.toMutableList())

    companion object {
        @JvmStatic
        fun from(model: EducationAddresses): EducationAddressesVO = with(model) {
            EducationAddressesVO(value.map { EducationAddressEntity.from(it) }.toMutableList())
        }
    }
}
