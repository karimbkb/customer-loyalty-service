package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.CustomerLoyaltyServiceApplication
import com.kb.customerloyaltyservice.enums.LoyaltyType.ORDER
import com.kb.customerloyaltyservice.enums.TransactionType.ADD
import com.kb.customerloyaltyservice.enums.TransactionType.SUBTRACT
import com.kb.customerloyaltyservice.mapper.ToPointsHistoryResponseDtoMapper
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.repository.PointsHistoryRepository
import com.kb.customerloyaltyservice.service.PointsHistoryService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.DELETE_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY_BY_CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_HISTORY_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_HISTORY_ID_2
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POST_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.REASON
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.REASON_2
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.getStringFromResources
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [CustomerLoyaltyServiceApplication::class],
)
@Testcontainers(disabledWithoutDocker = true)
@Sql(scripts = ["/sql/points-history/insert-points-history-data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/sql/points-history/delete-test-data.sql"])
class PointsHistoryControllerIntegrationTest {
    companion object {
        @JvmStatic
        @Container
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer<Nothing>("postgres:11.5-alpine").apply {
                withDatabaseName("postgres")
                withUsername("root")
                withPassword("root")
            }

        @JvmStatic
        @DynamicPropertySource
        fun properties(dynamicPropertyRegistry: DynamicPropertyRegistry) {
            dynamicPropertyRegistry.apply {
                add("spring.datasource.url") { container.jdbcUrl }
                add("spring.datasource.username") { container.username }
                add("spring.datasource.password") { container.password }
            }
        }
    }

    init {
        container.start()

        val configuration = ClassicConfiguration()
        configuration.setDataSource(container.jdbcUrl, container.username, container.password)
        configuration.setLocations(Location("classpath:db/migration"))

        val flyway = Flyway(configuration)
        flyway.migrate()
    }

    @Autowired private lateinit var pointsHistoryService: PointsHistoryService

    @Autowired private lateinit var pointsHistoryRepository: PointsHistoryRepository

    @Autowired private lateinit var customerPointsRepository: CustomerPointsRepository

    @Autowired
    private lateinit var toPointsHistoryResponseDtoMapper: ToPointsHistoryResponseDtoMapper

    @BeforeEach
    fun setUpRestAssured() {
        val mockMvc =
            MockMvcBuilders.standaloneSetup(
                PointsHistoryController(pointsHistoryService, toPointsHistoryResponseDtoMapper),
            )
                .build()
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Test
    fun `should get points history by id`() {
        RestAssuredMockMvc.given()
            .auth()
            .none()
            .`when`()[GET_POINTS_HISTORY.replace("{id}", POINTS_HISTORY_ID.toString())]
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(POINTS_HISTORY_ID.toString()))
            .body("pointsId", equalTo(POINTS_ID.toString()))
            .body("customerId", equalTo(CUSTOMER_ID.toString()))
            .body("points", equalTo(30))
            .body("transactionType", equalTo(ADD.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())
    }

    @Test
    fun `should get every points history by customer id`() {
        RestAssuredMockMvc.given()
            .auth()
            .none()
            .`when`()[
            GET_POINTS_HISTORY_BY_CUSTOMER_ID.replace("{customerId}", CUSTOMER_ID.toString()),
        ]
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.OK.value())
            // Entry 1
            .body("id[0]", equalTo(POINTS_HISTORY_ID.toString()))
            .body("pointsId[0]", equalTo(POINTS_ID.toString()))
            .body("customerId[0]", equalTo(CUSTOMER_ID.toString()))
            .body("points[0]", equalTo(30))
            .body("transactionType[0]", equalTo(ADD.toString()))
            .body("loyaltyType[0]", equalTo(ORDER.toString()))
            .body("reason[0]", equalTo(REASON))
            .body("createdAt[0]", notNullValue())
            // Entry 2
            .body("id[1]", equalTo(POINTS_HISTORY_ID_2.toString()))
            .body("pointsId[1]", equalTo(POINTS_ID.toString()))
            .body("customerId[1]", equalTo(CUSTOMER_ID.toString()))
            .body("points[1]", equalTo(10))
            .body("transactionType[1]", equalTo(ADD.toString()))
            .body("loyaltyType[1]", equalTo(ORDER.toString()))
            .body("reason[1]", equalTo(REASON_2))
            .body("createdAt[1]", notNullValue())
    }

    @Test
    fun `should create points for a customer by adding new points`() {
        val createPointsRequest = getStringFromResources("requests/add-points.json")

        RestAssuredMockMvc.given()
            .auth()
            .none()
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(createPointsRequest)
            .`when`()
            .post(POST_POINTS_HISTORY)
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue())
            .body("customerId", equalTo(CUSTOMER_ID.toString()))
            .body("points", equalTo(6))
            .body("transactionType", equalTo(ADD.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())

        customerPointsRepository.findByCustomerId(CUSTOMER_ID).let {
            assertThat(it).isNotNull
            assertThat(it?.totalPoints).isEqualTo(46)
            assertThat(it?.customerId).isEqualTo(CUSTOMER_ID)
            //            assertThat(it?.pointsHistory).hasSize(1)
        }

        val pointsHistory = pointsHistoryRepository.findAllByCustomerId(CUSTOMER_ID)
        assertThat(pointsHistory).hasSize(3)
        assertThat(pointsHistory[0].transactionType).isEqualTo(ADD)
        assertThat(pointsHistory[0].points).isEqualTo(30)
        assertThat(pointsHistory[1].transactionType).isEqualTo(ADD)
        assertThat(pointsHistory[1].points).isEqualTo(10)
        assertThat(pointsHistory[2].transactionType).isEqualTo(ADD)
        assertThat(pointsHistory[2].points).isEqualTo(6)
    }

    @Test
    fun `should create points for a customer by subtracting new points`() {
        val createPointsRequest = getStringFromResources("requests/subtract-points.json")

        RestAssuredMockMvc.given()
            .auth()
            .none()
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(createPointsRequest)
            .`when`()
            .post(POST_POINTS_HISTORY)
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue())
            .body("customerId", equalTo(CUSTOMER_ID.toString()))
            .body("points", equalTo(10))
            .body("transactionType", equalTo(SUBTRACT.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())

        customerPointsRepository.findByCustomerId(CUSTOMER_ID).let {
            assertThat(it).isNotNull
            assertThat(it?.totalPoints).isEqualTo(30)
            assertThat(it?.customerId).isEqualTo(CUSTOMER_ID)
            //            assertThat(it?.pointsHistory).hasSize(1)
        }

        val pointsHistory = pointsHistoryRepository.findAllByCustomerId(CUSTOMER_ID)
        assertThat(pointsHistory).hasSize(3)
        assertThat(pointsHistory[0].transactionType).isEqualTo(ADD)
        assertThat(pointsHistory[0].points).isEqualTo(30)
        assertThat(pointsHistory[1].transactionType).isEqualTo(ADD)
        assertThat(pointsHistory[1].points).isEqualTo(10)
        assertThat(pointsHistory[2].transactionType).isEqualTo(SUBTRACT)
        assertThat(pointsHistory[2].points).isEqualTo(10)
    }

    @Test
    fun `should delete points from the points history table by id`() {
        RestAssuredMockMvc.given()
            .auth()
            .none()
            .`when`()
            .delete(DELETE_POINTS_HISTORY, POINTS_HISTORY_ID.toString())
            .prettyPeek()
            .then()
            .statusCode(NO_CONTENT.value())

        assertThat(pointsHistoryRepository.findById(POINTS_HISTORY_ID)).isEmpty()
    }
}
