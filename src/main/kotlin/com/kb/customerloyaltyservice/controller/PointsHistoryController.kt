package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.dto.PointsHistoryResponseDTO
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.mapper.toPointsHistoryResponseDTO
import com.kb.customerloyaltyservice.service.PointsHistoryService
import mu.KLogging
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/points-history")
class PointsHistoryController(private val pointsHistoryService: PointsHistoryService) {

    companion object : KLogging()

    @GetMapping("/{id}")
    fun getCustomerPointsHistory(@PathVariable("id") id: UUID): PointsHistory {
        logger.info("Getting customer points record by id [{}]", id)
        return pointsHistoryService.getCustomerPointsHistoryById(id)
    }

    @GetMapping("/customer/{customerId}")
    fun getAllCustomerPointsHistory(@PathVariable("customerId") customerId: UUID): List<PointsHistory> {
        logger.info("Getting all customer points history by customer id [{}]", customerId)
        return pointsHistoryService.getAllCustomerPointsHistoryByCustomerId(customerId)
    }

    @PostMapping
    @ResponseStatus(CREATED)
    fun createPointsHistory(@RequestBody pointsHistoryCreateDTO: PointsHistoryCreateDTO): PointsHistoryResponseDTO {
        logger.info("Creating customer points history by with data [{}]", pointsHistoryCreateDTO)
        return pointsHistoryService.createPointsHistory(pointsHistoryCreateDTO).toPointsHistoryResponseDTO()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deletePointsHistory(@PathVariable id: UUID) {
        logger.info("Deleting customer points history by id [{}]", id)
        return pointsHistoryService.deletePointsHistory(id)
    }
}
