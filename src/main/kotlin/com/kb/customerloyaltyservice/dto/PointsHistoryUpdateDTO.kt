package com.kb.customerloyaltyservice.dto

import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import java.util.*

data class PointsHistoryUpdateDTO(
    val customerId: UUID,
    val points: Int,
    val transactionType: TransactionType,
    val loyaltyType: LoyaltyType,
    val reason: String,
)
