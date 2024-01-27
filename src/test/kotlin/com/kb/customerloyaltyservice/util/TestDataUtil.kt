package com.kb.customerloyaltyservice.util

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.UUID
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

@Suppress("UtilityClassWithPublicConstructor")
class TestDataUtil {
    companion object {
        const val GET_POINTS_HISTORY = "/api/v1/points-history/{id}"
        const val GET_POINTS_HISTORY_BY_CUSTOMER_ID = "/api/v1/points-history/customer/{customerId}"
        const val POST_POINTS_HISTORY = "/api/v1/points-history"
        const val DELETE_POINTS_HISTORY = "/api/v1/points-history/{id}"

        const val GET_CUSTOMER_POINTS_BY_CUSTOMER_ID = "/api/v1/customer-points/{customerId}"

        private val POINTS_ID_2: UUID = UUID.fromString("1da2748c-5f28-444f-9b0b-6b71b97d00a5")
        val POINTS_ID: UUID = UUID.fromString("40ef642b-8a0b-4737-a84f-f4deb551ed4e")
        val POINTS_HISTORY_ID: UUID = UUID.fromString("ac98b028-a523-4aa3-b8cc-2d0ef3a1223d")
        val POINTS_HISTORY_ID_2: UUID = UUID.fromString("ea10e9cc-eab8-434a-a756-56e4b6368d1f")
        val CUSTOMER_ID: UUID = UUID.fromString("419d5f27-758e-4aaa-81ee-ecf27074eaf3")
        val CUSTOMER_ID_2: UUID = UUID.fromString("cf1f7a3c-3ddc-44b0-ade4-b15d5a7d23c3")
        const val REASON: String = "Order with number #436272986"
        const val REASON_2: String = "Order with number #436272361"
        val CUSTOMER_POINTS_ID: UUID = UUID.fromString("40ef642b-8a0b-4737-a84f-f4deb551ed4e")

        fun buildPointsHistoryCreateDTO(): PointsHistoryCreateDTO {
            return PointsHistoryCreateDTO(
                customerId = CUSTOMER_ID,
                points = 6,
                transactionType = TransactionType.ADD,
                loyaltyType = LoyaltyType.ORDER,
                reason = REASON,
            )
        }

        fun buildPointsHistory(): PointsHistory {
            return PointsHistory(
                id = UUID.fromString("e39f442a-46bc-4ec1-99d9-e76a7cca737a"),
                pointsId = POINTS_ID,
                customerId = CUSTOMER_ID,
                points = 6,
                transactionType = TransactionType.ADD,
                loyaltyType = LoyaltyType.ORDER,
                reason = REASON,
                createdAt = LocalDateTime.now(),
            )
        }

        fun buildPointsHistoryList(): List<PointsHistory> {
            return listOf(
                PointsHistory(
                    id = UUID.fromString("e39f442a-46bc-4ec1-99d9-e76a7cca737a"),
                    pointsId = POINTS_ID,
                    customerId = CUSTOMER_ID,
                    points = 6,
                    transactionType = TransactionType.ADD,
                    loyaltyType = LoyaltyType.ORDER,
                    reason = REASON,
                    createdAt = LocalDateTime.now(),
                ),
                PointsHistory(
                    id = UUID.fromString("ce5a9cc4-f2aa-4b1b-9d08-a80dcbb98103"),
                    pointsId = POINTS_ID_2,
                    customerId = CUSTOMER_ID_2,
                    points = 3,
                    transactionType = TransactionType.SUBTRACT,
                    loyaltyType = LoyaltyType.MANUAL_ENTRY,
                    reason = REASON,
                    createdAt = LocalDateTime.now(),
                ),
            )
        }

        fun buildCustomerPoints(): CustomerPoints {
            return CustomerPoints(
                id = POINTS_ID,
                customerId = CUSTOMER_ID,
                totalPoints = 99,
                pointsHistory = emptyList(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }

        fun getStringFromResources(filePath: String): String {
            val inputStream = javaClass.classLoader.getResourceAsStream(filePath)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val jsonParser = JSONParser()
            val jsonObject = jsonParser.parse(reader) as JSONObject

            return jsonObject.toJSONString()
        }
    }
}
