package com.meloning.megaCoffee.core.domain.education.model

class EducationAddresses(
    private val _value: MutableList<EducationAddress>
) {
    val value: List<EducationAddress>
        get() = _value.toList()

    init {
        require(_value.isNotEmpty()) { "교육 장소는 반드시 1개 이상이어야 합니다." }
        require(_value.size <= MAX_EDUCATION_PLACE_COUNT) { "교육 장소는 최대 ${MAX_EDUCATION_PLACE_COUNT}개까지 등록할 수 있습니다." }
    }

    fun add(model: EducationAddress) {
        if (_value.size > MAX_EDUCATION_PLACE_COUNT) throw IllegalStateException("이미 교육장소들이 가득찼습니다.")
        _value.add(model)
    }

    fun remove(model: EducationAddress) {
        _value.remove(model)
    }

    companion object {
        const val MAX_EDUCATION_PLACE_COUNT = 3
    }
}
