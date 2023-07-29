package com.meloning.megaCoffee.core.exception

import com.meloning.megaCoffee.common.error.exception.ApplicationException

class AlreadyFullException : ApplicationException {
    override val code: String = "already_full"
    override val statusCode: Int = 409

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
