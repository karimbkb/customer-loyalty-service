package com.kb.customerloyaltyservice.mapper

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.dto.PointsHistoryResponseDTO
import com.kb.customerloyaltyservice.entity.PointsHistory

fun PointsHistory.toPointsHistoryResponseDTO(): PointsHistoryResponseDTO {
    return PointsHistoryResponseDTO(
        id = this.id,
        customerId = this.customerId,
        points = this.points,
        transactionType = this.transactionType,
        loyaltyType = this.loyaltyType,
        reason = this.reason,
        createdAt = this.createdAt
    )
}

fun PointsHistoryCreateDTO.toPointsHistory(): PointsHistory {
    return PointsHistory(
        null,
        null,
        customerId = this.customerId,
        points = this.points,
        transactionType = this.transactionType,
        loyaltyType = this.loyaltyType,
        reason = this.reason,
        null
    )
}
