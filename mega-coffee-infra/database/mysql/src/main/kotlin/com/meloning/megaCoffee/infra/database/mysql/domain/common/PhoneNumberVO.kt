package com.meloning.megaCoffee.infra.database.mysql.domain.common

import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class PhoneNumberVO(
    @Column(nullable = false)
    var phone: String
) {
    init {
        require(phone.isNotBlank()) { "휴대폰 번호는 비어있거나 공백일 수 없습니다." }
        require(PhoneNumber.pattern.matches(phone)) { "휴대폰 번호는 숫자조합 11자리여야 합니다." }
    }

    fun toModel() = PhoneNumber(phone)

    companion object {
        @JvmStatic
        fun from(model: PhoneNumber) = PhoneNumberVO(model.phone)
    }
}
