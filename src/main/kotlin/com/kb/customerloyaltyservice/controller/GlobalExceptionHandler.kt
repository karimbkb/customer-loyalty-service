package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.ValidationErrorResponse
import com.kb.customerloyaltyservice.dto.Violation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val error = ValidationErrorResponse(status = status.value())
        ex.bindingResult.fieldErrors.forEach { fieldError ->
            error.violations.add(Violation(fieldError.field, fieldError.defaultMessage))
        }
        return ResponseEntity(error, headers, status)
    }
}
