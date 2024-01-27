package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.mapper.ToPointsHistoryMapper
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistory
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryCreateDTO
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import java.util.Optional
import java.util.UUID
import javax.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PointsHistoryServiceTest {
    @MockK
    private lateinit var pointsHistoryRepository: PointsHistoryRepository

    @MockK
    private lateinit var customerPointsRepository: CustomerPointsRepository

    @Suppress("Unused", "UnusedPrivateProperty")
    private val toPointsHistoryMapper = ToPointsHistoryMapper()

    @InjectMockKs
    private lateinit var service: PointsHistoryService

    @Test
    fun `should return a single point data of a customer by id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryRepository.findById(pointsHistory.id!!) } returns
            Optional.of(pointsHistory)

        // when
        val result = service.getCustomerPointsHistoryById(pointsHistory.id!!)

        // then
        assertThat(result).isEqualTo(pointsHistory)
    }

    @Test
    fun `should throw EntityNotFoundException due to wrong id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryRepository.findById(pointsHistory.id!!) } returns Optional.empty()

        // when + then
        assertThatThrownBy { service.getCustomerPointsHistoryById(pointsHistory.id!!) }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessage("Points with id ${pointsHistory.id} could not be found.")
    }

    @Test
    fun `should return list of points history of a customer by customer id`() {
        // given
        val pointsHistory1 = buildPointsHistory()
        val pointsHistory2 = pointsHistory1.copy(points = 20)
        every { pointsHistoryRepository.findAllByCustomerId(CUSTOMER_ID) } returns
            listOf(pointsHistory1, pointsHistory2)

        // when
        val result = service.getAllCustomerPointsHistoryByCustomerId(CUSTOMER_ID)

        // then
        assertThat(result).hasSize(2).isEqualTo(listOf(pointsHistory1, pointsHistory2))
    }

    @Test
    fun `should create points history and update customer points`() {
        // given
        val pointsHistoryCreateDTO = buildPointsHistoryCreateDTO()
        val customerPoints = buildCustomerPoints()
        val pointsHistory = buildPointsHistory().copy(id = null, createdAt = null)
        every { customerPointsRepository.findByCustomerId(CUSTOMER_ID) } returns customerPoints
        every { customerPointsRepository.save(customerPoints) } returns customerPoints
        every { pointsHistoryRepository.save(pointsHistory) } returns pointsHistory

        // when
        val result = service.createPointsHistory(pointsHistoryCreateDTO)

        // then
        assertThat(result).isEqualTo(pointsHistory)
    }

    @Test
    fun `should create points history and save customer points`() {
        // given
        val pointsHistoryCreateDTO = buildPointsHistoryCreateDTO()
        val customerPoints =
            buildCustomerPoints()
                .copy(
                    id = null,
                    totalPoints = 6,
                    createdAt = null,
                    updatedAt = null,
                    pointsHistory = emptyList(),
                )
        val pointsHistory = buildPointsHistory().copy(id = null, createdAt = null, pointsId = null)
        every { customerPointsRepository.findByCustomerId(CUSTOMER_ID) } returns null
        every { customerPointsRepository.save(customerPoints) } returns customerPoints
        every { pointsHistoryRepository.save(pointsHistory) } returns pointsHistory

        // when
        val result = service.createPointsHistory(pointsHistoryCreateDTO)

        // then
        assertThat(result).isEqualTo(pointsHistory)
    }

    @Test
    fun `should delete customer points history`() {
        // given
        val id = UUID.randomUUID()
        every { pointsHistoryRepository.deleteById(id) } just runs

        // when
        service.deletePointsHistory(id)

        // then
        verify { pointsHistoryRepository.deleteById(id) }
    }
}
