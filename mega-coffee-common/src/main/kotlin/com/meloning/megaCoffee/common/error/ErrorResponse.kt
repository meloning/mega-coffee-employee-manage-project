package com.meloning.megaCoffee.common.error

data class ErrorResponse
private constructor(
    val code: String,
    val message: String,
    val errors: List<InputError>
) {

    data class InputError(
        val field: String,
        val message: String
    )

    companion object {
        @JvmStatic
        fun create(code: String, message: String): ErrorResponse {
            return ErrorResponse(code, message, emptyList())
        }

        @JvmStatic
        fun create(code: String, message: String, errors: List<InputError>): ErrorResponse {
            return ErrorResponse(code, message, errors)
        }
    }
}
