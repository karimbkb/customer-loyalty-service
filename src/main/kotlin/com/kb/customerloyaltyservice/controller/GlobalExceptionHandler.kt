package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.ValidationErrorResponse
import com.kb.customerloyaltyservice.dto.Violation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val error = ValidationErrorResponse()
        for (fieldError in e.bindingResult.fieldErrors) {
            error.violations.add(Violation(fieldError.field, fieldError.defaultMessage))
        }
        return ResponseEntity<ValidationErrorResponse>(error, HttpStatus.BAD_REQUEST)
    }
}
