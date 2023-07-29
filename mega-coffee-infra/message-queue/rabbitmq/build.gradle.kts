description = "mega-coffee-infra:message-queue:rabbitmq module"

dependencies {
    implementation(project(":mega-coffee-common"))
    compileOnly(project(":mega-coffee-core"))

    implementation("org.springframework.boot:spring-boot-starter-amqp")
}
