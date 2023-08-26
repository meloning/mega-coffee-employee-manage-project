package com.meloning.megaCoffee.core.domain.education.model

import com.meloning.megaCoffee.core.exception.AlreadyFullException
import com.meloning.megaCoffee.core.exception.ConflictFieldException
import com.meloning.megaCoffee.core.exception.ExpiredFieldException
import com.meloning.megaCoffee.core.exception.NotFoundException
import java.time.LocalDate
import java.time.LocalTime

class EducationPlaces(
    private val _value: MutableList<EducationPlace>
) {
    val value: List<EducationPlace>
        get() = _value.toList()

    init {
        validateDuplicatePlaceTimeSlots()
        require(!isMaxEducationPlaceCountExceeded()) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 등록할 수 있습니다." }
    }

    fun size(): Int {
        return _value.size
    }

    fun add(model: EducationPlace) {
        validateDuplicatePlaceTimeSlots(model)
        if (hasReachedMaxEducationPlaceCapacity()) throw AlreadyFullException("이미 교육장소들이 가득찼습니다.")
        _value.add(model)
    }

    fun remove(model: EducationPlace) {
        _value.remove(model)
    }

    fun validateExisting(educationPlaceIds: List<Long>) {
        if (!_value.mapNotNull { it.id }.containsAll(educationPlaceIds)) {
            throw NotFoundException("존재하지 않는 교육 장소들이 있습니다.")
        }
    }

    fun validateExpired(educationPlaceIds: List<Long>) {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val selectedEducationPlaces = filterByContainedIds(educationPlaceIds)
        val isExpiredEducationPlace = selectedEducationPlaces.any {
            it.date < currentDate || (it.date == currentDate && it.timeRange.endTime <= currentTime)
        }
        if (isExpiredEducationPlace) {
            throw ExpiredFieldException("선택한 교육장소는 이미 만료되었습니다.")
        }
    }

    fun findAndIncreaseCurrentParticipant(id: Long) {
        _value.find { it.id!! == id }?.increaseCurrentParticipant()
    }

    fun filterByContainedIds(educationPlaceIds: List<Long>): List<EducationPlace> {
        return _value.filter { educationPlaceIds.contains(it.id) }
    }

    private fun isMaxEducationPlaceCountExceeded(): Boolean {
        return size() > MAX_EDUCATION_PLACE_COUNT
    }

    private fun hasReachedMaxEducationPlaceCapacity(): Boolean {
        return size() == MAX_EDUCATION_PLACE_COUNT
    }

    private fun validateDuplicatePlaceTimeSlots(educationPlace: EducationPlace? = null) {
        val targets = educationPlace?.let { value.plus(educationPlace) } ?: _value
        if (hasDuplicatePlaceTimeSlots(targets)) {
            throw ConflictFieldException("장소, 날짜, 시간대가 겹쳐 등록할 수 없습니다.")
        }
    }

    private fun hasDuplicatePlaceTimeSlots(value: List<EducationPlace>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in value.indices) {
                if (i == j) continue
                val educationPlace1 = value[i]
                val educationPlace2 = value[j]

                if (!educationPlace1.isSameDatePlaceTimeSlots(educationPlace2)) continue
                else return true
            }
        }
        return false
    }

    companion object {
        const val MAX_EDUCATION_PLACE_COUNT = 5
    }
}
