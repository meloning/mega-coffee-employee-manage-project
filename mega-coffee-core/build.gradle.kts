description = "mega-coffee-core module"

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

dependencies {
    implementation(project(":mega-coffee-common"))
    implementation(project(":mega-coffee-core-infra"))

    compileOnly("org.springframework:spring-context")
    compileOnly("org.springframework:spring-tx")
}
