package com.meloning.megaCoffee.core.domain.education.model

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
        if (isMaxEducationPlaceCountExceeded()) throw IllegalStateException("이미 교육장소들이 가득찼습니다.")
        _value.add(model)
    }

    fun remove(model: EducationAddress) {
        _value.remove(model)
    }

    fun validateExisting(educationAddressIds: List<Long>) {
        if (!_value.mapNotNull { it.id }.containsAll(educationAddressIds)) {
            throw RuntimeException("존재하지 않는 교육 장소들이 있습니다.")
        }
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
            throw RuntimeException("장소, 날짜, 시간대가 겹쳐 등록할 수 없습니다.")
        }
    }

    private fun hasDuplicatePlaceTimeSlots(value: List<EducationAddress>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in value.indices) {
                if (i == j) continue
                val educationAddress1 = value[i]
                val educationAddress2 = value[j]

                if (!educationAddress1.isSameDateTimePlace(educationAddress2)) continue
                else return true
            }
        }
        return false
    }

    companion object {
        const val MAX_EDUCATION_PLACE_COUNT = 5
    }
}
