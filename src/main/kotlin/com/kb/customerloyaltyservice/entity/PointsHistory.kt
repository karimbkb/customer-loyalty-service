package com.kb.customerloyaltyservice.entity

import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.*
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
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID?,
    @Type(type = "pg-uuid")
    var pointsId: UUID?,
    @Type(type = "pg-uuid")
    val customerId: UUID,
    val points: Int,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,
    @Enumerated(EnumType.STRING)
    var loyaltyType: LoyaltyType,
    val reason: String,
    @CreationTimestamp
    val createdAt: LocalDateTime?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PointsHistory

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , pointsId = $pointsId , customerId = $customerId , " +
                "points = $points , transactionType = $transactionType , loyaltyType = $loyaltyType , " +
                "reason = $reason , createdAt = $createdAt )"
    }
}
