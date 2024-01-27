package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.mapper.ToPointsHistoryResponseDtoMapper
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.service.PointsHistoryService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.DELETE_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY_BY_CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_HISTORY_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POST_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistory
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryCreateDTO
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryList
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.getStringFromResources
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PointsHistoryController::class)
class PointsHistoryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var customerPointsRepository: CustomerPointsRepository

    @MockkBean
    private lateinit var pointsHistoryService: PointsHistoryService

    @Suppress("Unused", "UnusedPrivateProperty")
    @SpykBean
    private lateinit var toPointsHistoryResponseDtoMapper: ToPointsHistoryResponseDtoMapper

    @Test
    fun `should get points history of a customer by point id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryService.getCustomerPointsHistoryById(POINTS_ID) } returns pointsHistory

        // when + then
        mockMvc
            .perform(get(GET_POINTS_HISTORY, POINTS_ID).header(ACCEPT, APPLICATION_JSON))
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
        every { pointsHistoryService.getAllCustomerPointsHistoryByCustomerId(CUSTOMER_ID) } returns
            pointsHistory

        // when + then
        mockMvc
            .perform(
                get(GET_POINTS_HISTORY_BY_CUSTOMER_ID, CUSTOMER_ID).header(ACCEPT, APPLICATION_JSON),
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("e39f442a-46bc-4ec1-99d9-e76a7cca737a"))
            .andExpect(jsonPath("$[0].customerId").value("419d5f27-758e-4aaa-81ee-ecf27074eaf3"))
            .andExpect(jsonPath("$[0].points").value(6))
            .andExpect(jsonPath("$[0].transactionType").value("ADD"))
            .andExpect(jsonPath("$[0].loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$[0].reason").value("Order with number #436272986"))
            .andExpect(jsonPath("$[1].id").value("ce5a9cc4-f2aa-4b1b-9d08-a80dcbb98103"))
            .andExpect(jsonPath("$[1].customerId").value("cf1f7a3c-3ddc-44b0-ade4-b15d5a7d23c3"))
            .andExpect(jsonPath("$[1].points").value(3))
            .andExpect(jsonPath("$[1].transactionType").value("SUBTRACT"))
            .andExpect(jsonPath("$[1].loyaltyType").value("MANUAL_ENTRY"))
            .andExpect(jsonPath("$[1].reason").value("Order with number #436272986"))
            .andDo(print())
            .andReturn()
    }

    @Test
    fun `create points for a customer`() {
        // given
        val customerPoints = buildCustomerPoints()
        val pointsHistoryCreateDTO = buildPointsHistoryCreateDTO()
        val pointsHistory = buildPointsHistory()

        every { pointsHistoryService.createPointsHistory(pointsHistoryCreateDTO) } returns
            pointsHistory
        every {
            customerPointsRepository.findByCustomerId(pointsHistoryCreateDTO.customerId)
        } returns customerPoints

        // when + then
        mockMvc
            .perform(
                post(POST_POINTS_HISTORY)
                    .contentType(APPLICATION_JSON)
                    .content(getStringFromResources("requests/add-points.json")),
            )
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

    @Test
    fun `should delete points for a customer`() {
        // given
        every { pointsHistoryService.deletePointsHistory(POINTS_HISTORY_ID) } just runs
        every { customerPointsRepository.deleteById(POINTS_HISTORY_ID) } just runs

        // when + then
        mockMvc
            .perform(
                delete(DELETE_POINTS_HISTORY, POINTS_HISTORY_ID).header(ACCEPT, APPLICATION_JSON),
            )
            .andExpect(status().isNoContent())
            .andReturn()
    }
}
