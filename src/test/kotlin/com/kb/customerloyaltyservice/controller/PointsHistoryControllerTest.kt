package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.service.PointsHistoryService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID_2
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY_BY_CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POST_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistory
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryCreateDTO
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryList
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.getStringFromResources
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(PointsHistoryController::class)
class PointsHistoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var pointsHistoryService: PointsHistoryService

    @Test
    fun `get points history of a customer by point id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryService.getCustomerPointsHistoryById(POINTS_ID) } returns pointsHistory

        // when + then
        mockMvc
            .perform(
                get(GET_POINTS_HISTORY, POINTS_ID).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("e39f442a-46bc-4ec1-99d9-e76a7cca737a"))
            .andExpect(jsonPath("$.customerId").value("419d5f27-758e-4aaa-81ee-ecf27074eaf3"))
            .andExpect(jsonPath("$.points").value(6))
            .andExpect(jsonPath("$.transactionType").value("ADD"))
            .andExpect(jsonPath("$.loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$.reason").value("Order with number #436272986"))
            .andDo(print())
            .andReturn()
    }

    @Test
    fun `get the whole history of points of a customer by customer id`() {
        // given
        val pointsHistory = buildPointsHistoryList()
        every { pointsHistoryService.getAllCustomerPointsHistoryByCustomerId(CUSTOMER_ID) } returns pointsHistory

        // when + then
        mockMvc
            .perform(
                get(GET_POINTS_HISTORY_BY_CUSTOMER_ID, CUSTOMER_ID).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("e39f442a-46bc-4ec1-99d9-e76a7cca737a"))
            .andExpect(jsonPath("$[0].customerId").value(CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$[0].points").value(6))
            .andExpect(jsonPath("$[0].transactionType").value("ADD"))
            .andExpect(jsonPath("$[0].loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$[0].reason").value("Order with number #436272986"))
            .andExpect(jsonPath("$[1].id").value("ce5a9cc4-f2aa-4b1b-9d08-a80dcbb98103"))
            .andExpect(jsonPath("$[1].customerId").value(CUSTOMER_ID_2.toString()))
            .andExpect(jsonPath("$[1].points").value(3))
            .andExpect(jsonPath("$[1].transactionType").value("SUBTRACT"))
            .andExpect(jsonPath("$[1].loyaltyType").value("MANUAL_ENTRY"))
            .andExpect(jsonPath("$[1].reason").value(""))
            .andDo(print())
            .andReturn()
    }

    @Test
    fun `create points for a user`() {
        // given
        val pointsHistoryCreateDTO = buildPointsHistoryCreateDTO()
        val pointsHistory = buildPointsHistory()

        every { pointsHistoryService.createPointsHistory(pointsHistoryCreateDTO) } returns pointsHistory

        // when + then
        mockMvc
            .perform(
                post(POST_POINTS_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getStringFromResources("requests/create-points.json")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("e39f442a-46bc-4ec1-99d9-e76a7cca737a"))
            .andExpect(jsonPath("$.customerId").value("419d5f27-758e-4aaa-81ee-ecf27074eaf3"))
            .andExpect(jsonPath("$.points").value(6))
            .andExpect(jsonPath("$.transactionType").value("ADD"))
            .andExpect(jsonPath("$.loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$.reason").value("Order with number #436272986"))
            .andDo(print())
            .andReturn()
    }
}