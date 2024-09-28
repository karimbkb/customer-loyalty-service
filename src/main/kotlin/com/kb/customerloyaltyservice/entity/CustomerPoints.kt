package com.kb.customerloyaltyservice.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    val id: UUID? = null,
    val customerId: UUID,
    var totalPoints: Int,
    @JoinColumn(name = "pointsId", referencedColumnName = "id")
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true, fetch = FetchType.LAZY)
    val pointsHistory: List<PointsHistory> = emptyList(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 84738928374749292L
    }
}
