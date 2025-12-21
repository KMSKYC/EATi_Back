# 멀티스테이지 빌드를 사용하여 최적화된 이미지 생성
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Gradle 의존성 캐싱을 위해 먼저 복사
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

# 의존성 다운로드 (캐싱 레이어)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN gradle bootJar --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 보안을 위한 non-root 사용자 생성
RUN groupadd -r eati && useradd -r -g eati eati

# 빌드 스테이지에서 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 소유권 변경
RUN chown eati:eati app.jar

# non-root 사용자로 전환
USER eati

# 애플리케이션 포트
EXPOSE 8080

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
