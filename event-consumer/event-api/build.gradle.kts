description = "event-consumer:event-api module"

dependencies {
    implementation(project(":clients:java-email"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}
