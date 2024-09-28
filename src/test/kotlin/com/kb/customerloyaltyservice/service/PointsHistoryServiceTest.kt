package com.kb.customerloyaltyservice.service

import com.kb.customerloyaltyservice.mapper.ToPointsHistoryMapper
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildCustomerPoints
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistory
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.buildPointsHistoryCreateDTO
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
class PointsHistoryServiceTest {
    @MockK
    private lateinit var pointsHistoryRepository: PointsHistoryRepository

    @MockK
    private lateinit var customerPointsService: CustomerPointsService

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
        val result = service.getPointsHistoryById(pointsHistory.id!!)

        // then
        assertThat(result).isEqualTo(pointsHistory)
    }

    @Test
    fun `should throw EntityNotFoundException due to wrong id`() {
        // given
        val pointsHistory = buildPointsHistory()
        every { pointsHistoryRepository.findById(pointsHistory.id!!) } returns Optional.empty()

        // when + then
        assertThatThrownBy { service.getPointsHistoryById(pointsHistory.id!!) }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessage("Points with id ${pointsHistory.id} could not be found.")
    }

    @Test
    fun `should create points history and save customer points`() {
        // given
        val pointsHistoryCreateDTO = buildPointsHistoryCreateDTO()
        val pointsHistory = toPointsHistoryMapper(pointsHistoryCreateDTO)
        val customerPoints = buildCustomerPoints()
        every { customerPointsService.updateCustomerPoints(any()) } returns customerPoints
        every { pointsHistoryRepository.save(any()) } returns pointsHistory

        // when
        val result = service.createPointsHistory(pointsHistoryCreateDTO)

        // then
        assertThat(result).isEqualTo(pointsHistory)
    }
}
