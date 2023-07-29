package com.meloning.megaCoffee.core.exception

import com.meloning.megaCoffee.common.error.exception.ApplicationException

class AlreadyExistException : ApplicationException {
    override val code: String = "already_exist"
    override val statusCode: Int = 400

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
