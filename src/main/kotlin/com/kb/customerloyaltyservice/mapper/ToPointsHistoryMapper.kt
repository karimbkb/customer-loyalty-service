package com.kb.customerloyaltyservice.mapper

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.PointsHistory
import org.springframework.stereotype.Component

@Component
class ToPointsHistoryMapper : (PointsHistoryCreateDTO) -> PointsHistory {
    override fun invoke(pointsHistoryCreateDTO: PointsHistoryCreateDTO): PointsHistory {
        return PointsHistory(
            points = pointsHistoryCreateDTO.points,
            transactionType = pointsHistoryCreateDTO.transactionType,
            loyaltyType = pointsHistoryCreateDTO.loyaltyType,
            reason = pointsHistoryCreateDTO.reason,
        )
    }
}
