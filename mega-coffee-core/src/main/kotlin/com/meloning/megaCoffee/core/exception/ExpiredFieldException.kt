package com.meloning.megaCoffee.core.exception

import com.meloning.megaCoffee.common.error.exception.ApplicationException

class ExpiredFieldException : ApplicationException {
    override val code: String = "expired_field"
    override val statusCode: Int = 409

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
