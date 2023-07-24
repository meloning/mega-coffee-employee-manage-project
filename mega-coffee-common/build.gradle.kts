description = "mega-coffee-common module"

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

dependencies {
    val jakartaValidationVersion: String by project

    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("jakarta.validation:jakarta.validation-api:$jakartaValidationVersion")
}