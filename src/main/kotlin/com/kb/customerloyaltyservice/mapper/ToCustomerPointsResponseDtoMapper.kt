package com.kb.customerloyaltyservice.mapper

import com.kb.customerloyaltyservice.dto.CustomerPointsResponseDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints
import org.springframework.stereotype.Component

@Component
class ToCustomerPointsResponseDtoMapper : (CustomerPoints) -> CustomerPointsResponseDTO {
    override fun invoke(customerPoints: CustomerPoints): CustomerPointsResponseDTO {
        return CustomerPointsResponseDTO(
            id = customerPoints.id,
            customerId = customerPoints.customerId,
            totalPoints = customerPoints.totalPoints,
            createdAt = customerPoints.createdAt,
            updatedAt = customerPoints.updatedAt,
        )
    }
}
