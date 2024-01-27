package com.kb.customerloyaltyservice.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
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
    @Type(type = "pg-uuid") val customerId: UUID,
    var totalPoints: Int,
    @JoinColumn(name = "customerId")
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true, fetch = FetchType.EAGER)
    val pointsHistory: List<PointsHistory> = emptyList(),
    @CreationTimestamp val createdAt: LocalDateTime? = null,
    @UpdateTimestamp val updatedAt: LocalDateTime? = null,
)
