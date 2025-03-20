plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.nsu.burym"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}


allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-webflux")
		implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
		implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
		implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
		implementation("org.glassfish.jaxb:jaxb-xjc:4.0.0")

		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.projectreactor:reactor-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")


	}
}
//tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//	archiveBaseName.set("gs-producing-web-service")
//	archiveVersion.set("0.1.0")
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
