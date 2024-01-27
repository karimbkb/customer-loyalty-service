package com.kb.customerloyaltyservice.repository

import com.kb.customerloyaltyservice.entity.CustomerPoints
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerPointsRepository : JpaRepository<CustomerPoints, UUID> {
    fun findByCustomerId(customerId: UUID): CustomerPoints?
}
