package com.meloning.megaCoffee.domain.common.dto

import com.meloning.megaCoffee.core.domain.common.Address
import javax.validation.constraints.NotBlank

data class AddressRequest(
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val city: String,
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val street: String,
    @field:NotBlank(message = "비어있거나 공백이 포함되어 있습니다.")
    val zipCode: String
) {

    fun toModel() = Address(city, street, zipCode)
}
