package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.exception.CustomerNotFoundException
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CustomerPointsServiceTest {
    @MockK
    private lateinit var customerPointsRepository: CustomerPointsRepository

    @InjectMockKs
    private lateinit var service: CustomerPointsService

    @Test
    fun `should return customer points summary by customer id`() {
        // given
        val customerPoints = buildCustomerPoints()
        every { customerPointsRepository.findByCustomerId(CUSTOMER_ID) } returns customerPoints

        // when
        val result = service.getCustomerPointsByCustomerId(CUSTOMER_ID)

        // then
        assertThat(result).isEqualTo(customerPoints)
    }

    @Test
    fun `should throw CustomerNotFoundException when customer points summary is null`() {
        // given
        every { customerPointsRepository.findByCustomerId(CUSTOMER_ID) } returns null

        // when + then
        assertThatThrownBy { service.getCustomerPointsByCustomerId(CUSTOMER_ID) }
            .isInstanceOf(CustomerNotFoundException::class.java)
            .hasMessage("Customer Points for customer with id $CUSTOMER_ID could not be found.")
    }
}
