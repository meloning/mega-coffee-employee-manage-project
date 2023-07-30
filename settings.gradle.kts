pluginManagement {
    val kotlinVersion: String by settings
    val kotlinterVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        // https://github.com/jeremymailen/kotlinter-gradle/releases
        id("org.jmailen.kotlinter") version kotlinterVersion

        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
    }
}

rootProject.name = "mega-coffee-employee-manage-project"

include(
    "mega-coffee-api",
    "mega-coffee-common",
    "mega-coffee-core",
    "mega-coffee-infra:database:mysql",
    "mega-coffee-infra:message-queue:rabbitmq",
    "clients:java-email",
    "event-consumer:event-api"
)
