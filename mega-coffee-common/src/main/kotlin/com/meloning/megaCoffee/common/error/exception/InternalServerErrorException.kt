package com.meloning.megaCoffee.common.error.exception

class InternalServerErrorException : ApplicationException {
    override val code: String = "internal_server_error"
    override val statusCode: Int = 500

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
