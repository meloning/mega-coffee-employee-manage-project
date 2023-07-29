package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.common.Address

data class AddressResponse(
    val city: String,
    val street: String,
    val zipCode: String
) {

    companion object {
        @JvmStatic
        fun from(model: Address) = with(model) {
            AddressResponse(city, street, zipCode)
        }
    }
}
