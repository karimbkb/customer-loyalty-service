package com.kb.customerloyaltyservice.util

import com.kb.customerloyaltyservice.dto.PointsHistoryCreateDTO
import com.kb.customerloyaltyservice.entity.CustomerPoints
import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.*

class TestDataUtil {
    companion object {
        const val GET_POINTS_HISTORY = "/api/v1/points-history/{id}"
        const val GET_POINTS_HISTORY_BY_CUSTOMER_ID = "/api/v1/points-history/customer/{customerId}"
        const val POST_POINTS_HISTORY = "/api/v1/points-history"
        const val SHORT_URL_DELETE = "/api/v1/points-history"

        const val GET_CUSTOMER_POINTS_BY_CUSTOMER_ID = "/api/v1/customer-points/{customerId}"

        val POINTS_ID = UUID.fromString("bc3e671d-06b3-49ed-8608-11b058d7af04")
        val POINTS_ID_2 = UUID.fromString("1da2748c-5f28-444f-9b0b-6b71b97d00a5")
        val CUSTOMER_ID = UUID.fromString("419d5f27-758e-4aaa-81ee-ecf27074eaf3")
        val CUSTOMER_ID_2 = UUID.fromString("cf1f7a3c-3ddc-44b0-ade4-b15d5a7d23c3")

        val CUSTOMER_POINTS_ID = UUID.fromString("40ef642b-8a0b-4737-a84f-f4deb551ed4e")

        fun buildPointsHistoryCreateDTO(): PointsHistoryCreateDTO {
            return PointsHistoryCreateDTO(
                customerId = CUSTOMER_ID,
                points = 6,
                transactionType = TransactionType.ADD,
                loyaltyType = LoyaltyType.ORDER,
                reason = "Order with number #436272986"
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
                reason = "Order with number #436272986",
                createdAt = LocalDateTime.now()
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
                    reason = "Order with number #436272986",
                    createdAt = LocalDateTime.now()
                ),
                PointsHistory(
                    id = UUID.fromString("ce5a9cc4-f2aa-4b1b-9d08-a80dcbb98103"),
                    pointsId = POINTS_ID_2,
                    customerId = CUSTOMER_ID_2,
                    points = 3,
                    transactionType = TransactionType.SUBTRACT,
                    loyaltyType = LoyaltyType.MANUAL_ENTRY,
                    reason = "",
                    createdAt = LocalDateTime.now()
                )
            )
        }

        fun buildCustomerPoints(): CustomerPoints {
            return CustomerPoints(
                id = CUSTOMER_POINTS_ID,
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