description = "mega-coffee-scheduler module"

dependencies {
    val mysqlVersion: String by project
    val hikaricpVersion: String by project

    implementation(project(":mega-coffee-common"))
    implementation(project(":mega-coffee-core-infra"))

    runtimeOnly(project(":mega-coffee-infra:message-queue:rabbitmq"))

    runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")

    implementation("org.springframework:spring-web")
    implementation("org.apache.httpcomponents:httpclient")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")
}
