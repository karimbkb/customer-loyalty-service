package com.kb.customerloyaltyservice.dto

import java.time.LocalDateTime
import java.util.*

data class CustomerPointsResponseDTO(
    val id: UUID?,
    val customerId: UUID,
    val totalPoints: Int,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
