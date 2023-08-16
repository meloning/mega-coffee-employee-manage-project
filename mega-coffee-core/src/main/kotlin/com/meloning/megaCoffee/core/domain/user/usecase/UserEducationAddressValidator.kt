package com.meloning.megaCoffee.core.domain.user.usecase

import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import com.meloning.megaCoffee.core.exception.ConflictFieldException

object UserEducationAddressValidator {

    fun validateDuplicateTimeSlots(targets: List<EducationAddress>) {
        if (hasDuplicateTimeSlots(targets)) {
            val ids = targets.map { it.id }
            throw ConflictFieldException("선택한 교육 장소들의 날짜와 시간이 겹쳐 등록할 수 없습니다. [$ids]")
        }
    }

    fun validateAlreadyRegister(userEducationAddresses: List<EducationAddress>, selectedEducationAddressIds: List<Long>) {
        val duplicatedValues = userEducationAddresses.filter { selectedEducationAddressIds.contains(it.id) }
        if (duplicatedValues.isNotEmpty()) {
            throw AlreadyExistException("이미 등록한 교육장소를 신청하였습니다. 재등록 바랍니다.")
        }
    }

    private fun hasDuplicateTimeSlots(value: List<EducationAddress>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in value.indices) {
                if (i == j) continue
                val educationAddress1 = value[i]
                val educationAddress2 = value[j]

                if (!educationAddress1.isSameDateTimeSlots(educationAddress2)) continue
                else return true
            }
        }
        return false
    }
}
