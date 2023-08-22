description = "mega-coffee-infra:database:mysql module"

apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

tasks {
    bootJar { enabled = false }
    jar { enabled = true }
}

val querydslGeneratedDir = "$buildDir/generated"
val querydslKtOutputSubDirFull = "$querydslGeneratedDir/source/kaptKotlin"

sourceSets {
    main {
        java {
            srcDir(querydslKtOutputSubDirFull)
        }
    }
}

dependencies {
    val mysqlVersion: String by project
    val querydslVersion: String by project
    val guavaVersion: String by project

    implementation(project(":mega-coffee-common"))
    compileOnly(project(":mega-coffee-core"))

    api("org.springframework:spring-web")
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // https://github.com/spring-projects/spring-retry
    api("org.springframework.retry:spring-retry")

    implementation("com.querydsl:querydsl-jpa")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")

    testImplementation(project(":mega-coffee-core"))

    testFixturesApi("com.google.guava:guava:$guavaVersion")
    testFixturesApi("org.testcontainers:mysql")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
