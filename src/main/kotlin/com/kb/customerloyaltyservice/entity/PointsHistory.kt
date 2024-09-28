package com.kb.customerloyaltyservice.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
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
    val id: UUID? = null,
    var pointsId: UUID? = null,
    val points: Int,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,
    @Enumerated(EnumType.STRING)
    var loyaltyType: LoyaltyType,
    val reason: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)
