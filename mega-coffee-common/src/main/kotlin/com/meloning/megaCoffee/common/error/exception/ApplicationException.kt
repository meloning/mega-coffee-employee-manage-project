package com.meloning.megaCoffee.common.error.exception

interface Status {
    val statusCode: Int
}
abstract class ApplicationException : RuntimeException, Status {
    open val code: String = "application"
    override val statusCode: Int = 500

    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
