package com.kb.customerloyaltyservice.service


import com.kb.customerloyaltyservice.entity.PointsHistory
import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class PointsHistoryServiceTest {

    @MockBean
    private lateinit var pointsHistoryRepository: PointsHistoryRepository

    @MockBean
    private lateinit var customerPointsRepository: CustomerPointsRepository

    private lateinit var service: PointsHistoryService

    @BeforeEach
    fun setup() {
        service = PointsHistoryService(pointsHistoryRepository, customerPointsRepository)
    }

    @Test
    //given
    fun `getCustomerPointsHistoryById should return points history`() {
        val pointsHistory = PointsHistory(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 10, TransactionType.ADD, LoyaltyType.MANUAL_ENTRY, "order customer", LocalDateTime.now())
        every { pointsHistoryRepository.findById(pointsHistory.id!!) } returns Optional.of(pointsHistory)

        //when
        val result = service.getCustomerPointsHistoryById(pointsHistory.id!!)

        //then
        assertEquals(pointsHistory, result)
    }

//    @Test
//    //given
//    fun `getAllCustomerPointsHistoryByCustomerId should return list of points history`() {
//        val customerId = UUID.randomUUID()
//        val pointsHistory1 = PointsHistory(UUID.randomUUID(), customerId, 10)
//        val pointsHistory2 = PointsHistory(UUID.randomUUID(), customerId, 20)
//        every { pointsHistoryRepository.findAllByCustomerId(customerId) } returns listOf(pointsHistory1, pointsHistory2)
//
//        //when
//        val result = service.getAllCustomerPointsHistoryByCustomerId(customerId)
//
//        //then
//        assertEquals(listOf(pointsHistory1, pointsHistory2), result)
//    }
//
//    @Test
//    //given
//    fun `createPointsHistory should create points history and update customer points`() {
//        val customerId = UUID.randomUUID()
//        val pointsHistoryCreateDTO = PointsHistoryCreateDTO(customerId, 10)
//        val customerPoints = CustomerPoints(UUID.randomUUID(), customerId, 10)
//        val pointsHistory = PointsHistory(UUID.randomUUID(), customerId, 10)
//        every { customerPointsRepository.findByCustomerId(customerId) } returns customerPoints
//        every { customerPointsRepository.save(customerPoints) } returns customerPoints
//        every { pointsHistoryRepository.save(pointsHistory) } returns pointsHistory
//
//        //when
//        val result = service.createPointsHistory(pointsHistoryCreateDTO)
//
//        //then
//        assertEquals(pointsHistory, result)
//    }

    @Test
    //given
    fun `deletePointsHistory should delete customer points history`() {
        val id = UUID.randomUUID()

        //when
        service.deletePointsHistory(id)

        //then
        // verify that deleteById was called with the correct id
        verify { customerPointsRepository.deleteById(id) }
    }
}