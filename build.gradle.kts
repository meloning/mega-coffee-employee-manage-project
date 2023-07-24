import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * [Plugin Collect]
 * - 사용할 플러그인을 정의하는 곳이다.
 */
plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false

    id("org.jmailen.kotlinter")

    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jmailen.kotlinter")

    group = "com.meloning"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

val kopringProjects = listOf(
    project(":mega-coffee-api"),
    project(":mega-coffee-core"),
    project(":mega-coffee-common"),
    project(":mega-coffee-infra:database:mysql")
)
configure(kopringProjects) {
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

val querydslProjects = listOf(
    project(":mega-coffee-infra:database:mysql")
)
configure(querydslProjects) {
    val querydslVersion: String by project

    dependencies {
        kapt("com.querydsl:querydsl-kotlin-codegen:$querydslVersion")
        kapt("org.springframework.boot:spring-boot-configuration-processor")
    }
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}


