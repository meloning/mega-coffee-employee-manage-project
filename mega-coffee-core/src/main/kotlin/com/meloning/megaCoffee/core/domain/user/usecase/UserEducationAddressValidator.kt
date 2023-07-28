package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.core.domain.education.model.EducationAddress

object UserEducationAddressValidator {

    fun validateDuplicateTimeSlots(targets: List<EducationAddress>) {
        if (hasDuplicateTimeSlots(targets)) {
            val ids = targets.map { it.id }
            throw RuntimeException("선택한 교육 장소들의 날짜와 시간이 겹쳐 등록할 수 없습니다. [$ids]")
        }
    }

    private fun hasDuplicateTimeSlots(value: List<EducationAddress>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in i + 1 until value.size) {
                val educationAddress1 = value[i]
                val educationAddress2 = value[j]

                return educationAddress1.isSameDateTimeSlots(educationAddress2)
            }
        }
        return false
    }
}
