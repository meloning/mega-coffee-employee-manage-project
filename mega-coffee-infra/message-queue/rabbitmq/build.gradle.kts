description = "mega-coffee-infra:message-queue:rabbitmq module"

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

dependencies {
    implementation(project(":mega-coffee-common"))
    compileOnly(project(":mega-coffee-core-infra"))

    api("org.springframework.boot:spring-boot-starter-amqp")

    testFixturesApi("org.testcontainers:rabbitmq")
}
