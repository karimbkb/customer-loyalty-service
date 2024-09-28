package com.kb.customerloyaltyservice.dto

import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import java.time.LocalDateTime
import java.util.UUID

data class PointsHistoryResponseDTO(
    val id: UUID?,
    val points: Int,
    val transactionType: TransactionType,
    val loyaltyType: LoyaltyType,
    val reason: String,
    val createdAt: LocalDateTime?,
)
