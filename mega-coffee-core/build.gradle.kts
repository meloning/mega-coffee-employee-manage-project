description = "mega-coffee-core module"

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

dependencies {
    implementation(project(":mega-coffee-common"))
    compileOnly("org.springframework:spring-context")
    compileOnly("org.springframework:spring-tx")
}