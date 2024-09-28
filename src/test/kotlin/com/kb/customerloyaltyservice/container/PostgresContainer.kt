package com.kb.customerloyaltyservice.container

import org.slf4j.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

open class PostgresContainer : PostgreSQLContainer<PostgresContainer>(IMAGE) {
    companion object {
        private const val IMAGE_VERSION = "11.5-alpine"
        private val IMAGE = DockerImageName.parse("postgres:$IMAGE_VERSION")

        @JvmStatic
        @Container
        val postgresSQLContainer: PostgreSQLContainer<*> =
            PostgresContainer().apply {
                withDatabaseName("postgres")
                withUsername("root")
                withPassword("root")
                withLoggerName("postgres")
            }

        @JvmStatic
        @DynamicPropertySource
        fun properties(dynamicPropertyRegistry: DynamicPropertyRegistry) {
            dynamicPropertyRegistry.apply {
                add("spring.datasource.url") { postgresSQLContainer.jdbcUrl }
                add("spring.datasource.username") { postgresSQLContainer.username }
                add("spring.datasource.password") { postgresSQLContainer.password }
            }
        }
    }

    private fun <T : PostgreSQLContainer<T>> T.withLoggerName(name: String): T =
        withLogConsumer(
            Slf4jLogConsumer(
                LoggerFactory.getLogger(name),
            ),
        )
}
