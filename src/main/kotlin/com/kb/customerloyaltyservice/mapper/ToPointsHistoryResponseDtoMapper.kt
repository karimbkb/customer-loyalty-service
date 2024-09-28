package com.kb.customerloyaltyservice.mapper

import com.kb.customerloyaltyservice.dto.PointsHistoryResponseDTO
import com.kb.customerloyaltyservice.entity.PointsHistory
import org.springframework.stereotype.Component

@Component
class ToPointsHistoryResponseDtoMapper : (PointsHistory) -> PointsHistoryResponseDTO {
    override fun invoke(pointsHistory: PointsHistory): PointsHistoryResponseDTO {
        return PointsHistoryResponseDTO(
            id = pointsHistory.id,
            points = pointsHistory.points,
            transactionType = pointsHistory.transactionType,
            loyaltyType = pointsHistory.loyaltyType,
            reason = pointsHistory.reason,
            createdAt = pointsHistory.createdAt,
        )
    }
}
