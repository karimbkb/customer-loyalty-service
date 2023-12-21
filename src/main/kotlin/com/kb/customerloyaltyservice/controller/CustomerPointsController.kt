package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.dto.CustomerPointsResponseDTO
import com.kb.customerloyaltyservice.mapper.toCustomerPointsResponseDTO
import com.kb.customerloyaltyservice.service.CustomerPointsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/customer-points")
class CustomerPointsController(private val customerPointsService: CustomerPointsService) {

    @GetMapping("/{customerId}")
    fun getCustomerPointsByCustomerId(@PathVariable("customerId") customerId: UUID): CustomerPointsResponseDTO =
        customerPointsService.getCustomerPointsByCustomerId(customerId).toCustomerPointsResponseDTO()
}
