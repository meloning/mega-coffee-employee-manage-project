package com.meloning.megaCoffee.core.exception

import com.meloning.megaCoffee.common.error.exception.ApplicationException

class NotFoundException : ApplicationException {
    override val code: String = "not_found"
    override val statusCode: Int = 404

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
