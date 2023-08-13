package com.meloning.megaCoffee

import com.meloning.megaCoffee.infra.database.mysql.config.DatabaseCleanup
import com.meloning.megaCoffee.infra.messageQueue.rabbitmq.config.RabbitMQContainerExtension
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(RabbitMQContainerExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest {
    @Autowired
    lateinit var databaseCleanup: DatabaseCleanup

    @LocalServerPort
    private val port = 0
    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
            databaseCleanup.afterPropertiesSet()
        }
        databaseCleanup.execute()
    }
}
