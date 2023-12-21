package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.mapper.toPointsHistory
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class PointsHistoryService(
    private val pointsHistoryRepository: PointsHistoryRepository,
    private val customerPointsRepository: CustomerPointsRepository
) {

    companion object : KLogging()

    fun getCustomerPointsHistoryById(id: UUID): PointsHistory = pointsHistoryRepository.findById(id)
        .orElseThrow { EntityNotFoundException("Points with id $id could not be found.") }

    fun getAllCustomerPointsHistoryByCustomerId(customerId: UUID): List<PointsHistory> =
        pointsHistoryRepository.findAllByCustomerId(customerId)

    fun createPointsHistory(pointsHistoryCreateDTO: PointsHistoryCreateDTO): PointsHistory {
        logger.debug("Saving new points to history with data [{}]", pointsHistoryCreateDTO)
        val pointsHistory = pointsHistoryCreateDTO.toPointsHistory()
        val customerPoints = updateCustomerPoints(pointsHistory)
        pointsHistory.pointsId = customerPoints.id
        return pointsHistoryRepository.save(pointsHistory)
    }

    fun deletePointsHistory(id: UUID) {
        logger.debug("Deleting customer points history by id [{}]", id)
        customerPointsRepository.deleteById(id)
    }

    private fun updateCustomerPoints(pointsHistory: PointsHistory): CustomerPoints {
        return customerPointsRepository.findByCustomerId(pointsHistory.customerId)?.also {
            it.totalPoints += pointsHistory.points
            customerPointsRepository.save(it)
        } ?: run {
            val customerPoints = CustomerPoints(
                customerId = pointsHistory.customerId,
                totalPoints = pointsHistory.points
            )
            customerPointsRepository.save(customerPoints)
        }
    }
}
