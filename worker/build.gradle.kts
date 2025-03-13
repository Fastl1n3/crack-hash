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
    implementation("com.github.dpaukov:combinatoricslib3:3.4.0")
   // implementation("org.apache.httpcomponents:httpclient:4.4.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("startScripts") {
    dependsOn("bootJar")
}