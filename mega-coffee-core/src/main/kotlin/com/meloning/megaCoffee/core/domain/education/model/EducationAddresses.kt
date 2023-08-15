package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.exception.AlreadyFullException
import com.meloning.megaCoffee.core.exception.ConflictFieldException
import com.meloning.megaCoffee.core.exception.ExpiredFieldException
import com.meloning.megaCoffee.core.exception.NotFoundException
import java.time.LocalDate
import java.time.LocalTime

class EducationAddresses(
    private val _value: MutableList<EducationAddress>
) {
    val value: List<EducationAddress>
        get() = _value.toList()

    init {
        validateDuplicatePlaceTimeSlots()
        require(isMaxEducationPlaceCountExceeded()) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 등록할 수 있습니다." }
    }

    fun size(): Int {
        return _value.size
    }

    fun add(model: EducationAddress) {
        validateDuplicatePlaceTimeSlots(model)
        if (isMaxEducationPlaceCountExceeded()) throw AlreadyFullException("이미 교육장소들이 가득찼습니다.")
        _value.add(model)
    }

    fun remove(model: EducationAddress) {
        _value.remove(model)
    }

    fun validateExisting(educationAddressIds: List<Long>) {
        if (!_value.mapNotNull { it.id }.containsAll(educationAddressIds)) {
            throw NotFoundException("존재하지 않는 교육 장소들이 있습니다.")
        }
    }

    fun validateExpired(educationAddressIds: List<Long>) {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val selectedEducationAddresses = filterByContainedIds(educationAddressIds)
        val isExpiredEducationAddress = selectedEducationAddresses.any {
            it.date < currentDate || (it.date == currentDate && it.timeRange.endTime <= currentTime)
        }
        if (isExpiredEducationAddress) {
            throw ExpiredFieldException("선택한 교육장소는 이미 만료되었습니다.")
        }
    }

    fun findAndIncreaseCurrentParticipant(id: Long) {
        _value.find { it.id!! == id }?.increaseCurrentParticipant()
    }

    fun filterByContainedIds(educationAddressIds: List<Long>): List<EducationAddress> {
        return _value.filter { educationAddressIds.contains(it.id) }
    }

    private fun isMaxEducationPlaceCountExceeded(): Boolean {
        return size() <= MAX_EDUCATION_PLACE_COUNT
    }

    private fun validateDuplicatePlaceTimeSlots(educationAddress: EducationAddress? = null) {
        val targets = educationAddress?.let { value.plus(educationAddress) } ?: _value
        if (hasDuplicatePlaceTimeSlots(targets)) {
            throw ConflictFieldException("장소, 날짜, 시간대가 겹쳐 등록할 수 없습니다.")
        }
    }

    private fun hasDuplicatePlaceTimeSlots(value: List<EducationAddress>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in value.indices) {
                if (i == j) continue
                val educationAddress1 = value[i]
                val educationAddress2 = value[j]

                if (!educationAddress1.isSameDatePlaceTimeSlots(educationAddress2)) continue
                else return true
            }
        }
        return false
    }

    companion object {
        const val MAX_EDUCATION_PLACE_COUNT = 5
    }
}
