package com.kb.customerloyaltyservice.controller

import com.kb.customerloyaltyservice.CustomerLoyaltyServiceApplication
import com.kb.customerloyaltyservice.container.PostgresContainer
import com.kb.customerloyaltyservice.enums.LoyaltyType.ORDER
import com.kb.customerloyaltyservice.enums.TransactionType.ADD
import com.kb.customerloyaltyservice.enums.TransactionType.SUBTRACT
import com.kb.customerloyaltyservice.mapper.ToPointsHistoryResponseDtoMapper
import com.kb.customerloyaltyservice.repository.CustomerPointsRepository
import com.kb.customerloyaltyservice.service.PointsHistoryService
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.CUSTOMER_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.GET_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_HISTORY_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POINTS_ID
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.POST_POINTS_HISTORY
import com.kb.customerloyaltyservice.util.TestDataUtil.Companion.REASON
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
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [CustomerLoyaltyServiceApplication::class],
)
@Testcontainers(disabledWithoutDocker = true)
@Sql(scripts = ["/sql/points-history/insert-points-history-data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/sql/points-history/delete-test-data.sql"])
class PointsHistoryControllerIntegrationTest : PostgresContainer() {
    init {
        setupFlywayMigrationConfig()
    }

    @Autowired
    private lateinit var pointsHistoryService: PointsHistoryService

    @Autowired
    private lateinit var customerPointsRepository: CustomerPointsRepository

    @Autowired
    @Suppress("UnusedPrivateProperty")
    private lateinit var toPointsHistoryResponseDtoMapper: ToPointsHistoryResponseDtoMapper

    @BeforeEach
    fun setUpRestAssured() {
        val mockMvc =
            MockMvcBuilders
                .standaloneSetup(PointsHistoryController(pointsHistoryService, toPointsHistoryResponseDtoMapper))
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
            .body("points", equalTo(30))
            .body("transactionType", equalTo(ADD.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())
    }

    @Test
    @Transactional
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
            .body("points", equalTo(6))
            .body("transactionType", equalTo(ADD.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())

        val customerPoints = customerPointsRepository.findByCustomerId(CUSTOMER_ID)
        assertThat(customerPoints).isNotNull
        assertThat(customerPoints?.totalPoints).isEqualTo(46)
        assertThat(customerPoints?.customerId).isEqualTo(CUSTOMER_ID)
        // One-to-Many Relationship
        assertThat(customerPoints?.pointsHistory).hasSize(3)
        assertThat(customerPoints?.pointsHistory?.get(0)?.transactionType).isEqualTo(ADD)
        assertThat(customerPoints?.pointsHistory?.get(0)?.points).isEqualTo(30)
        assertThat(customerPoints?.pointsHistory?.get(1)?.transactionType).isEqualTo(ADD)
        assertThat(customerPoints?.pointsHistory?.get(1)?.points).isEqualTo(10)
        assertThat(customerPoints?.pointsHistory?.get(2)?.transactionType).isEqualTo(ADD)
        assertThat(customerPoints?.pointsHistory?.get(2)?.points).isEqualTo(6)
    }

    @Test
    @Transactional
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
            .body("points", equalTo(10))
            .body("transactionType", equalTo(SUBTRACT.toString()))
            .body("loyaltyType", equalTo(ORDER.toString()))
            .body("reason", equalTo(REASON))
            .body("createdAt", notNullValue())

        val customerPoints = customerPointsRepository.findByCustomerId(CUSTOMER_ID)

        assertThat(customerPoints).isNotNull
        assertThat(customerPoints?.totalPoints).isEqualTo(30)
        assertThat(customerPoints?.customerId).isEqualTo(CUSTOMER_ID)
        // One-to-Many Relationship
        assertThat(customerPoints?.pointsHistory).hasSize(3)
        assertThat(customerPoints?.pointsHistory?.get(0)?.transactionType).isEqualTo(ADD)
        assertThat(customerPoints?.pointsHistory?.get(0)?.points).isEqualTo(30)
        assertThat(customerPoints?.pointsHistory?.get(1)?.transactionType).isEqualTo(ADD)
        assertThat(customerPoints?.pointsHistory?.get(1)?.points).isEqualTo(10)
        assertThat(customerPoints?.pointsHistory?.get(2)?.transactionType).isEqualTo(SUBTRACT)
        assertThat(customerPoints?.pointsHistory?.get(2)?.points).isEqualTo(10)
    }

    private fun setupFlywayMigrationConfig() {
        val configuration = ClassicConfiguration()
        configuration.setDataSource(
            postgresSQLContainer.jdbcUrl,
            postgresSQLContainer.username,
            postgresSQLContainer.password,
        )
        configuration.setLocations(Location("classpath:db/migration"))

        val flyway = Flyway(configuration)
        flyway.migrate()
    }
}
