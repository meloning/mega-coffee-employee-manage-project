import com.epages.restdocs.apispec.gradle.OpenApi3Extension
import com.epages.restdocs.apispec.gradle.OpenApi3Task
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * [Plugin Collect]
 * - 사용할 플러그인을 정의하는 곳이다.
 */
plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false

    id("org.jmailen.kotlinter")
    id("java-test-fixtures")

    id("com.epages.restdocs-api-spec") apply false

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

    tasks.formatKotlinMain {
        exclude {
            it.file.path.contains("generated/")
        }
    }

    tasks.lintKotlinMain {
        exclude { it.file.path.contains("generated/")}
    }
}

val kopringProjects = listOf(
    project(":mega-coffee-scheduler"),
    project(":mega-coffee-api"),
    project(":mega-coffee-core"),
    project(":mega-coffee-core-infra"),
    project(":mega-coffee-common"),
    project(":mega-coffee-infra:database:mysql"),
    project(":mega-coffee-infra:message-queue:rabbitmq"),
    project(":clients:java-email"),
    project(":event-consumer:event-api")
)
configure(kopringProjects) {
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    val mockitoKotlinVersion: String by project

    the<DependencyManagementExtension>().apply {
        imports {
            val testContainersVersion: String by project
            mavenBom("org.testcontainers:testcontainers-bom:${testContainersVersion}")
        }
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    }
}

val integrationTestProjects = listOf(
    project(":mega-coffee-infra:database:mysql"),
    project(":mega-coffee-infra:message-queue:rabbitmq")
)
configure(integrationTestProjects) {
    apply(plugin = "java-test-fixtures")

    dependencies {
        testFixturesApi("org.testcontainers:testcontainers")
        testFixturesApi("org.testcontainers:junit-jupiter")
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

val restDocsProjects = listOf(
    project(":mega-coffee-api"), // Admin API가 추가될 수 있으니 list로 미리 선언
)
configure(restDocsProjects) {
    apply(plugin = "com.epages.restdocs-api-spec")

    extra["snippetsDir"] = file("build/generated-snippets")

    val snippetsDir = extra["snippetsDir"] as File
    val initialDocsFile = File("${project.file("src/test/resources")}/docs/initial-overview.md")

    val epagesApiDocsVersion: String by project

    dependencies {
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

        testImplementation("com.epages:restdocs-api-spec:$epagesApiDocsVersion")
        testImplementation("com.epages:restdocs-api-spec-mockmvc:$epagesApiDocsVersion")
    }

    /**
     * @see <a href="https://github.com/ePages-de/restdocs-api-spec">Spring REST Docs API specification Integration</a>
     */
    configure<OpenApi3Extension> {
        setServer("http://localhost:9000")
        title = "MGC Employee Manage OAS"
        version = "1.0.0"
        description = initialDocsFile.readText()
        format = "yaml"
        outputFileNamePrefix = "incomplete_openapi3"
    }

    tasks.withType(OpenApi3Task::class) {
        finalizedBy("removeBlockScalarsFromYaml")
    }

    tasks.register("removeBlockScalarsFromYaml") {
        dependsOn("openapi3")
        doLast {
            val inputFile = File("${project.buildDir}/api-spec/incomplete_openapi3.yaml")
            val outputFile = File("${project.buildDir}/api-spec/openapi3.yaml")

            val yamlData = inputFile.readText()

            val cleanedYamlData = yamlData.replace(Regex("value: \\|-"), "value:")

            outputFile.writeText(cleanedYamlData)
        }
    }
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}
