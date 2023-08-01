description = "clients:java-email module"

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
