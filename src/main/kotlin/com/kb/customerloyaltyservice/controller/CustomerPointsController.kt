package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.CustomerPointsResponseDTO
import com.kb.customerloyaltyservice.mapper.ToCustomerPointsResponseDtoMapper
import com.kb.customerloyaltyservice.service.CustomerPointsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/customer-points")
@Api(
    value = "Loyalty Service - Customer Points",
    description = "This controller handles the accumulated points for customers",
)
class CustomerPointsController(
    private val customerPointsService: CustomerPointsService,
    private val toCustomerPointsResponseDtoMapper: ToCustomerPointsResponseDtoMapper,
) {
    @GetMapping("/{customerId}")
    @ApiOperation(value = "Get accumulated points and various other information by customer id")
    fun getCustomerPointsByCustomerId(
        @PathVariable("customerId") customerId: UUID,
    ): CustomerPointsResponseDTO =
        toCustomerPointsResponseDtoMapper(
            customerPointsService.getCustomerPointsByCustomerId(customerId),
        )
}
