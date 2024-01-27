package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.mapper.ToCustomerPointsResponseDtoMapper
import com.kb.customerloyaltyservice.service.CustomerPointsService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_CUSTOMER_POINTS_BY_CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CustomerPointsController::class)
class CustomerPointsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var customerPointsService: CustomerPointsService

    @Suppress("Unused")
    @SpykBean
    private lateinit var toCustomerPointsResponseDtoMapper: ToCustomerPointsResponseDtoMapper

    @Test
    fun `get customer points by customer id`() {
        // given
        val customerPoints = buildCustomerPoints()
        every { customerPointsService.getCustomerPointsByCustomerId(CUSTOMER_ID) } returns
            customerPoints

        // when + then
        mockMvc
            .perform(
                get(GET_CUSTOMER_POINTS_BY_CUSTOMER_ID, CUSTOMER_ID)
                    .header(ACCEPT, APPLICATION_JSON),
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("40ef642b-8a0b-4737-a84f-f4deb551ed4e"))
            .andExpect(jsonPath("$.customerId").value("419d5f27-758e-4aaa-81ee-ecf27074eaf3"))
            .andExpect(jsonPath("$.totalPoints").value(99))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }
}
