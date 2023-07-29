description = "event-server module"

dependencies {
    implementation(project(":mega-coffee-infra:message-queue:rabbitmq"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
