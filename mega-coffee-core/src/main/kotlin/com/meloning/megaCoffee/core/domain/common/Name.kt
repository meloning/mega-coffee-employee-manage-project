package com.meloning.megaCoffee.core.domain.common

data class Name(
    val value: String
) {
    init {
        require(value.isNotBlank()) { "이름은 비어있거나 공백일 수 없습니다." }
    }

    companion object {
        @JvmField
        val DUMMY = Name("더미")
    }
}
