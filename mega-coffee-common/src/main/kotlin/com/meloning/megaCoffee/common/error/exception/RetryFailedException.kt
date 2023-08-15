package com.meloning.megaCoffee.common.error.exception

class RetryFailedException : ApplicationException {
    override val code: String = "retry_failed"
    override val statusCode: Int = 500

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
