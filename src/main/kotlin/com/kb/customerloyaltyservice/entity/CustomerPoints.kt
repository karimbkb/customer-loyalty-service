package com.kb.customerloyaltyservice.entity

import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity(name = "customer_points")
@Table(name = "customer_points", schema = "customer_loyalty")
data class CustomerPoints(
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID? = null,
    @Type(type = "pg-uuid")
    val customerId: UUID,
    var totalPoints: Int,
    @JoinColumn(name = "customerId")
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val pointsHistory: List<PointsHistory> = emptyList(),
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CustomerPoints

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , customerId = $customerId , totalPoints = $totalPoints , " +
                "createdAt = $createdAt , updatedAt = $updatedAt )"
    }
}
