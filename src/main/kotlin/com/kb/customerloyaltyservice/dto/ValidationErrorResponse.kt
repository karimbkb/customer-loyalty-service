package com.kb.customerloyaltyservice.dto

data class ValidationErrorResponse(val violations: MutableList<Violation> = mutableListOf())
