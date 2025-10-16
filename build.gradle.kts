plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.spring") version "2.2.0"
	id("org.springframework.boot") version "3.5.7-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

group = "pl.rmaciak"
version = "0.0.1-SNAPSHOT"
description = "Certification project for 10xdev"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	// Spring Boot starters
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-quartz")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc") // HikariCP included

	// Kotlin support
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Database
	implementation("org.postgresql:postgresql")
	implementation("org.jetbrains.exposed:exposed-core:0.58.0")
	implementation("org.jetbrains.exposed:exposed-dao:0.58.0")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.58.0")
	implementation("org.jetbrains.exposed:exposed-java-time:0.58.0")
	implementation("org.liquibase:liquibase-core")

	// AWS SDK
	implementation(platform("software.amazon.awssdk:bom:2.29.42"))
	implementation("software.amazon.awssdk:s3")
	implementation("software.amazon.awssdk:ses")

	// Image processing
	implementation("net.coobird:thumbnailator:0.4.20")

	// Logging
	implementation("net.logstash.logback:logstash-logback-encoder:8.0")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito", module = "mockito-core")
		exclude(group = "org.mockito", module = "mockito-junit-jupiter")
	}
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:postgresql")

	// Kotest
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

	// MockK
	testImplementation("io.mockk:mockk:1.13.14")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

ktlint {
	version.set("1.5.0")
	verbose.set(true)
	android.set(false)
	outputToConsole.set(true)
	outputColorName.set("RED")
	ignoreFailures.set(false)
	enableExperimentalRules.set(true)

	filter {
		exclude("**/generated/**")
		exclude("**/build/**")
	}
}
