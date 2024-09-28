package com.kb.customerloyaltyservice.dto

import java.time.LocalDateTime

data class ValidationErrorResponse(
    val status: Int? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val violations: MutableList<Violation> = mutableListOf(),
)
