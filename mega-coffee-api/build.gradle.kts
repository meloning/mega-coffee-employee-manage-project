description = "mega-coffee-api module"

dependencies {
    val restAssuredVersion: String by project

    implementation(project(":mega-coffee-common"))
    implementation(project(":mega-coffee-core"))
    runtimeOnly(project(":mega-coffee-infra:database:mysql"))
    runtimeOnly(project(":mega-coffee-infra:message-queue:rabbitmq"))

    compileOnly("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation(testFixtures(project(":mega-coffee-infra:database:mysql")))
    testImplementation(testFixtures(project(":mega-coffee-infra:message-queue:rabbitmq")))
}
