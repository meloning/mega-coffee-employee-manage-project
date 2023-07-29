package com.meloning.megaCoffee.core.exception

import com.meloning.megaCoffee.common.error.exception.ApplicationException

class ConflictFieldException : ApplicationException {
    override val code: String = "conflict_field"
    override val statusCode: Int = 409

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
