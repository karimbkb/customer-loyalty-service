package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.dto.PointsHistoryResponseDTO
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.mapper.ToPointsHistoryResponseDtoMapper
import com.kb.customerloyaltyservice.service.PointsHistoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KLogging
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/points-history")
@Api(
    value = "Loyalty Service - Points History",
    description = "This controller handles the history of points for customers",
)
class PointsHistoryController(
    private val pointsHistoryService: PointsHistoryService,
    private val toPointsHistoryResponseDtoMapper: ToPointsHistoryResponseDtoMapper,
) {
    companion object : KLogging()

    @GetMapping("/{id}")
    @ApiOperation(value = "Get points history by id")
    fun getCustomerPointsHistory(
        @PathVariable("id") id: UUID,
    ): PointsHistory {
        logger.info("Getting customer points record by id [{}]", id)
        return pointsHistoryService.getPointsHistoryById(id)
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create new points")
    fun createPointsHistory(
        @Valid @RequestBody pointsHistoryCreateDTO: PointsHistoryCreateDTO,
    ): PointsHistoryResponseDTO {
        logger.info("Creating customer points history by with data [{}]", pointsHistoryCreateDTO)
        return toPointsHistoryResponseDtoMapper(
            pointsHistoryService.createPointsHistory(pointsHistoryCreateDTO),
        )
    }
}
