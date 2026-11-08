plugins {
    java
    id("io.qameta.allure") version "4.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.rest-assured:rest-assured:6.3.0")

    testImplementation(platform("io.qameta.allure:allure-bom:3.1.1"))
    testImplementation("io.qameta.allure:allure-jupiter")
    
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val withDocker = providers.gradleProperty("withDocker")
    .map(String::toBoolean)
    .getOrElse(false)

val composeFile = layout.projectDirectory.file(
    "docker/docker-compose.yml"
)

val composeProject = "nc-automation-tests"

val dockerDown = tasks.register<Exec>("dockerDown") {
    group = "docker"
    description = "Stop Docker Compose and remove resources"

    commandLine(
        "docker", "compose",
        "-p", composeProject,
        "-f", composeFile.asFile.absolutePath,
        "down",
        "--volumes",
        "--remove-orphans",
        "--rmi", "local"
    )
}

val dockerUp = tasks.register<Exec>("dockerUp") {
    group = "docker"
    description = "Start Docker Compose and wait for readiness"

    commandLine(
        "docker", "compose",
        "-p", composeProject,
        "-f", composeFile.asFile.absolutePath,
        "up",
        "--build",
        "--wait",
        "--wait-timeout", "120"
    )

    finalizedBy(dockerDown)
}

tasks.test {
    useJUnitPlatform()

    outputs.upToDateWhen { false }

    if (withDocker) {
        dependsOn(dockerUp)
        finalizedBy(dockerDown)
    }
}
