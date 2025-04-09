plugins {
    java
    application
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.nsu.burym"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":generated-model"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("startScripts") {
    dependsOn("bootJar")
}

tasks.register<JavaExec>("xjc") {
    group = "build"

    val xsdPath = "src/main/resources/xsd"
    val outputDir = "src/main/java"
    val packageName = "ru.nsu.burym.crack_hash.manager.model.generated"

    mainClass.set("com.sun.tools.xjc.XJCFacade")

    classpath = sourceSets.main.get().runtimeClasspath
    args = listOf(
        xsdPath,
        "-d", outputDir,
        "-p", packageName
    )
}