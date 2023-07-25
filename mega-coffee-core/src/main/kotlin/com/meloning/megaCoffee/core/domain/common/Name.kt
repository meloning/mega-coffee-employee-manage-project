package com.meloning.megaCoffee.core.domain.common

data class Name(
    val name: String
) {
    init {
        require(name.isNotBlank()) { "이름은 비어있거나 공백일 수 없습니다." }
    }
}
