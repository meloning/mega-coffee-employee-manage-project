package com.meloning.megaCoffee.error

import com.meloning.megaCoffee.common.error.ErrorResponse
import com.meloning.megaCoffee.common.error.exception.ApplicationException
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleRequestBodyError(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val bindingResult = exception.bindingResult
        val fieldErrors = bindingResult.fieldErrors.map {
            ErrorResponse.InputError(it.field, it.defaultMessage!!)
        }
        return ResponseEntity.badRequest()
            .body(ErrorResponse.create(INPUT_VALID_CODE, DEFAULT_INPUT_VALID_MESSAGE, fieldErrors))
    }

    @ExceptionHandler(BindException::class)
    fun handleQueryStringError(exception: BindException): ResponseEntity<ErrorResponse> {
        val bindingResult = exception.bindingResult
        val fieldErrors = bindingResult.fieldErrors.map {
            ErrorResponse.InputError(it.field, it.defaultMessage!!)
        }
        return ResponseEntity.badRequest()
            .body(ErrorResponse.create(INPUT_VALID_CODE, DEFAULT_INPUT_VALID_MESSAGE, fieldErrors))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val code = "illegal_argument"
        return ResponseEntity.badRequest()
            .body(ErrorResponse.create(code, exception.localizedMessage))
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(exception: ApplicationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(exception.statusCode)
            .body(ErrorResponse.create(exception.code, exception.message ?: exception.localizedMessage))
    }

    companion object {
        const val INPUT_VALID_CODE = "input_validation"
        const val DEFAULT_INPUT_VALID_MESSAGE = "An error occurred in the process of validating the input value."
    }
}
