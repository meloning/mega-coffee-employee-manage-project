package com.meloning.megaCoffee.core.domain.common

data class Address(
    val city: String,
    val street: String,
    val zipCode: String
) {
    init {
        require(city.isNotBlank()) { "도시명은 비어있거나 공백일 수 없습니다." }
        require(street.isNotBlank()) { "거리명은 비어있거나 공백일 수 없습니다." }
        require(zipCode.isNotBlank()) { "우편번호는 비어있거나 공백일 수 없습니다." }
    }

    companion object {
        @JvmField
        val DUMMY = Address("도시", "거리", "12345")
    }
}
