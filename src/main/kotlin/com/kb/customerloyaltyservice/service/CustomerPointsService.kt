package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.entity.CustomerPoints
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
}
