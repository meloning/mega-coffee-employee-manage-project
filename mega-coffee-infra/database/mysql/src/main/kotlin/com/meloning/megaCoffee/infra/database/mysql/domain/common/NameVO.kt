package com.meloning.megaCoffee.infra.database.mysql.domain.common

import com.meloning.megaCoffee.core.domain.common.Name
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class NameVO(
    @Column(nullable = false)
    val name: String
) {
    init {
        require(name.isNotBlank()) { "이름은 비어있거나 공백일 수 없습니다." }
    }

    fun toModel() = Name(name)

    companion object {
        @JvmStatic
        fun from(model: Name) = NameVO(model.name)
    }
}
