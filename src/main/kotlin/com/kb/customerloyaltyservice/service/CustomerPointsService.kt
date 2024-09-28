package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints
import com.kb.customerloyaltyservice.enums.TransactionType.ADD
import com.kb.customerloyaltyservice.enums.TransactionType.SUBTRACT
import com.kb.customerloyaltyservice.exception.CustomerNotFoundException
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerPointsService(private val customerPointsRepository: CustomerPointsRepository) {
    fun getCustomerPointsByCustomerId(customerId: UUID): CustomerPoints =
        customerPointsRepository.findByCustomerId(customerId)
            ?: throw CustomerNotFoundException(
                "Customer Points for customer with id $customerId could not be found.",
            )

    fun updateCustomerPoints(pointsHistoryCreateDTO: PointsHistoryCreateDTO): CustomerPoints {
        return customerPointsRepository.findByCustomerId(pointsHistoryCreateDTO.customerId)?.let { customerPoints ->
            when (pointsHistoryCreateDTO.transactionType) {
                ADD -> calculateAddedPoints(customerPoints, pointsHistoryCreateDTO)
                SUBTRACT -> calculateSubtractedPoints(customerPoints, pointsHistoryCreateDTO)
            }
            customerPointsRepository.save(customerPoints)
        }
            ?: run {
                val customerPoints =
                    CustomerPoints(
                        customerId = pointsHistoryCreateDTO.customerId,
                        totalPoints = pointsHistoryCreateDTO.points,
                    )
                customerPointsRepository.save(customerPoints)
            }
    }

    private fun calculateAddedPoints(
        customerPoints: CustomerPoints,
        pointsHistoryCreateDTO: PointsHistoryCreateDTO,
    ) {
        customerPoints.totalPoints += pointsHistoryCreateDTO.points
    }

    private fun calculateSubtractedPoints(
        customerPoints: CustomerPoints,
        pointsHistoryCreateDTO: PointsHistoryCreateDTO,
    ) = if ((customerPoints.totalPoints - pointsHistoryCreateDTO.points) < 0) {
        customerPoints.totalPoints = 0
    } else {
        customerPoints.totalPoints -= pointsHistoryCreateDTO.points
    }
}
