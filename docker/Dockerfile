# Multi-stage Dockerfile for DietMe backend

# Stage 1: Builder
FROM gradle:8.10-jdk21 AS builder

WORKDIR /app

# Copy Gradle files for dependency caching
COPY ../gradle gradle
COPY ../gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY ../.editorconfig ./

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY ../src src

# Build application (skip tests in Docker build for faster builds)
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

USER spring:spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
