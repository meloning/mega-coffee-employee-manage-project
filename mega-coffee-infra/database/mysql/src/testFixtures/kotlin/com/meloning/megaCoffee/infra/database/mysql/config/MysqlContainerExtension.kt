package com.meloning.megaCoffee.infra.database.mysql.config

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.LoggerFactory
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container

object MysqlContainerExtension : Extension, BeforeAllCallback, AfterAllCallback {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private const val MYSQL8_IMAGE_NAME = "mysql:8"

    @Container
    val mysql = MySQLContainer(MYSQL8_IMAGE_NAME)

    override fun beforeAll(context: ExtensionContext?) {
        if (mysql.isRunning()) return
        mysql.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        if (mysql.isRunning()) mysql.stop()
    }
}
