package com.kb.customerloyaltyservice.mapper

import com.kb.customerloyaltyservice.dto.CustomerPointsResponseDTO
import com.kb.customerloyaltyservice.dto.PointsHistoryResponseDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints

fun CustomerPoints.toCustomerPointsResponseDTO(): CustomerPointsResponseDTO {
    return CustomerPointsResponseDTO(
        id = this.id,
        customerId = this.customerId,
        totalPoints = this.totalPoints,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}
