plugins {
    id("java")
}

group = "ru.nsu.burym"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-xjc:4.0.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.register<JavaExec>("xjc") {
    group = "build"

    val xsdPath = "src/main/resources/xsd"
    val outputDir = "src/main/java"
    val packageName = "ru.nsu.burym.crack_hash.model.generated"

    mainClass.set("com.sun.tools.xjc.XJCFacade")

    classpath = sourceSets.main.get().runtimeClasspath
    args = listOf(
        xsdPath,
        "-d", outputDir,
        "-p", packageName
    )
}

tasks.test {
    useJUnitPlatform()
}