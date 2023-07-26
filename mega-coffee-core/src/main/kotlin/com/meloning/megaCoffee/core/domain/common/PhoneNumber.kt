package com.meloning.megaCoffee.core.domain.common

data class PhoneNumber(
    var phone: String
) {
    init {
        require(phone.isNotBlank()) { "휴대폰 번호는 비어있거나 공백일 수 없습니다." }
        require(pattern.matches(phone)) { "휴대폰 번호는 숫자조합 11자리여야 합니다." }
    }

    companion object {
        @JvmField
        val pattern = Regex("^[0-9]{11}\$")

        @JvmField
        val DUMMY = PhoneNumber("01012341234")
    }
}
