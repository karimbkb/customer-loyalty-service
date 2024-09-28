package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.mapper.ToPointsHistoryResponseDtoMapper
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.service.PointsHistoryService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POST_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistory
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryCreateDTO
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.getStringFromResources
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.hasEntry
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
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

    @SpykBean
    @Suppress("Unused", "UnusedPrivateProperty")
    private lateinit var toPointsHistoryResponseDtoMapper: ToPointsHistoryResponseDtoMapper

    @Test
    fun `should get points history of a customer by point id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryService.getPointsHistoryById(POINTS_ID) } returns pointsHistory

        // when + then
        mockMvc
            .perform(get(GET_POINTS_HISTORY, POINTS_ID).header(ACCEPT, APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("e39f442a-46bc-4ec1-99d9-e76a7cca737a"))
            .andExpect(jsonPath("$.points").value(6))
            .andExpect(jsonPath("$.transactionType").value("ADD"))
            .andExpect(jsonPath("$.loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$.reason").value("Order with number #436272986"))
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
            .andExpect(jsonPath("$.points").value(6))
            .andExpect(jsonPath("$.transactionType").value("ADD"))
            .andExpect(jsonPath("$.loyaltyType").value("ORDER"))
            .andExpect(jsonPath("$.reason").value("Order with number #436272986"))
            .andDo(print())
            .andReturn()
    }

    @Test
    fun `create points for a customer throws error messages`() {
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
                    .content(getStringFromResources("requests/add-points-failed-due-to-bean-validation.json")),
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.timestamp").isNotEmpty)
            .andExpect(
                jsonPath(
                    "$.violations",
                    hasItem(
                        allOf(
                            hasEntry("fieldName", "reason"),
                            hasEntry("message", "must not be empty"),
                        ),
                    ),
                ),
            )
            .andExpect(
                jsonPath(
                    "$.violations",
                    hasItem(
                        allOf(
                            hasEntry("fieldName", "points"),
                            hasEntry("message", "must be greater than or equal to 0"),
                        ),
                    ),
                ),
            )
            .andDo(print())
            .andReturn()
    }
}
