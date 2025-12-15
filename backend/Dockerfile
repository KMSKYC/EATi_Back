# Multi-stage build for Spring Boot backend
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy gradle files
COPY --chown=gradle:gradle settings.gradle.kts .
COPY --chown=gradle:gradle backend/build.gradle.kts backend/

# Copy source code
COPY --chown=gradle:gradle backend/src backend/src

# Build the application
WORKDIR /app/backend
RUN gradle clean bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -g 1000 eati && \
    adduser -D -u 1000 -G eati eati

# Copy jar from builder
COPY --from=builder --chown=eati:eati /app/backend/build/libs/*.jar app.jar

USER eati

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
