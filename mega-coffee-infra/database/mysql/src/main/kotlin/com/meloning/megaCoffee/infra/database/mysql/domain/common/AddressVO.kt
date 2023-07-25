package com.meloning.megaCoffee.infra.database.mysql.domain.common

import com.meloning.megaCoffee.core.domain.common.Address
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AddressVO(
    @Column(nullable = false)
    var city: String,
    @Column(nullable = false)
    var street: String,
    @Column(nullable = false)
    var zipCode: String
) {
    init {
        require(city.isNotBlank()) { "도시명은 비어있거나 공백일 수 없습니다." }
        require(street.isNotBlank()) { "거리명은 비어있거나 공백일 수 없습니다." }
        require(zipCode.isNotBlank()) { "우편번호는 비어있거나 공백일 수 없습니다." }
    }

    fun toModel() = Address(city, street, zipCode)

    companion object {
        @JvmStatic
        fun from(model: Address) = with(model) {
            AddressVO(city, street, zipCode)
        }
    }
}
