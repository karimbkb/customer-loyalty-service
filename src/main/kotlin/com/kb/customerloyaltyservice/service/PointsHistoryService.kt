package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.mapper.ToPointsHistoryMapper
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
class PointsHistoryService(
    private val pointsHistoryRepository: PointsHistoryRepository,
    private val customerPointsService: CustomerPointsService,
    private val toPointsHistoryMapper: ToPointsHistoryMapper,
) {
    companion object : KLogging()

    fun getPointsHistoryById(id: UUID): PointsHistory =
        pointsHistoryRepository.findById(id).orElseThrow {
            logger.error("Points history does not exist for points id: $id")
            EntityNotFoundException("Points with id $id could not be found.")
        }

    fun createPointsHistory(pointsHistoryCreateDTO: PointsHistoryCreateDTO): PointsHistory {
        logger.info("Saving new points to history with data [{}]", pointsHistoryCreateDTO)
        val pointsHistory = toPointsHistoryMapper(pointsHistoryCreateDTO)
        customerPointsService.updateCustomerPoints(pointsHistoryCreateDTO).also {
            pointsHistory.pointsId = it.id
            return pointsHistoryRepository.save(pointsHistory)
        }
    }
}
