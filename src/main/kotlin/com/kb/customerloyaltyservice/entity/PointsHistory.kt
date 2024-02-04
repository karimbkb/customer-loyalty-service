package com.kb.customerloyaltyservice.entity

import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "points_history")
@Table(name = "points_history", schema = "customer_loyalty")
data class PointsHistory(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID?,
    var pointsId: UUID?,
    val customerId: UUID,
    val points: Int,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,
    @Enumerated(EnumType.STRING)
    var loyaltyType: LoyaltyType,
    val reason: String,
    @CreationTimestamp
    val createdAt: LocalDateTime?,
)
