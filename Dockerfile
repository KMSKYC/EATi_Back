# JAR 파일만 복사하는 경량 이미지
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 보안을 위한 non-root 사용자 생성
RUN groupadd -r eati && useradd -r -g eati eati

# GitHub Actions에서 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

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
