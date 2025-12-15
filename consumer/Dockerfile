# Multi-stage build for Kafka Consumer
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy gradle files
COPY --chown=gradle:gradle settings.gradle.kts .
COPY --chown=gradle:gradle consumer/build.gradle.kts consumer/

# Copy source code
COPY --chown=gradle:gradle consumer/src consumer/src

# Build the application
WORKDIR /app/consumer
RUN gradle clean bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -g 1000 eati && \
    adduser -D -u 1000 -G eati eati

# Copy jar from builder
COPY --from=builder --chown=eati:eati /app/consumer/build/libs/*.jar app.jar

USER eati

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
