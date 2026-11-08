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

tasks.test {
    useJUnitPlatform()
}