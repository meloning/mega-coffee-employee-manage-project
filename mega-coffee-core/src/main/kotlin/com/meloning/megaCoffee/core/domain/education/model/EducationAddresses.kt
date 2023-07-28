package com.meloning.megaCoffee.core.domain.education.model

class EducationAddresses(
    private val _value: MutableList<EducationAddress>
) {
    val value: List<EducationAddress>
        get() = _value.toList()

    init {
        require(_value.isNotEmpty()) { "교육 장소는 반드시 1개 이상이어야 합니다." }
        require(_value.size <= MAX_EDUCATION_PLACE_COUNT) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 등록할 수 있습니다." }
        validateDuplicatePlaceTimeSlots()
    }

    fun add(model: EducationAddress) {
        if (_value.size > MAX_EDUCATION_PLACE_COUNT) throw IllegalStateException("이미 교육장소들이 가득찼습니다.")
        _value.add(model)
    }

    fun remove(model: EducationAddress) {
        _value.remove(model)
    }

    fun validateExisting(educationAddressIds: List<Long>) {
        val missingEducationAddresses = value.filterNot { educationAddressIds.contains(it.id) }.map { it.id }
        if (missingEducationAddresses.isNotEmpty()) {
            throw RuntimeException("존재하지 않는 교육 장소들이 있습니다. [$missingEducationAddresses]")
        }
    }

    fun filterByContainedIds(educationAddressIds: List<Long>): List<EducationAddress> {
        return _value.filter { educationAddressIds.contains(it.id) }
    }

    fun validateDuplicatePlaceTimeSlots() {
        if (hasDuplicatePlaceTimeSlots(_value)) {
            throw RuntimeException("장소, 날짜, 시간대가 겹쳐 등록할 수 없습니다.")
        }
    }

    private fun hasDuplicatePlaceTimeSlots(value: List<EducationAddress>): Boolean {
        for (i in 0 until value.size - 1) {
            for (j in i + 1 until value.size) {
                val educationAddress1 = value[i]
                val educationAddress2 = value[j]

                return educationAddress1.isSameDateTimePlace(educationAddress2)
            }
        }
        return false
    }

    companion object {
        const val MAX_EDUCATION_PLACE_COUNT = 5
    }
}
