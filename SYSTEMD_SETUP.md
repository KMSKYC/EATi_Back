# Systemd 방식 서버 구성 가이드

백엔드와 컨슈머는 systemd로, Kafka/Zookeeper는 Docker로 구성하는 가이드입니다.

## 🏗 아키텍처

```
┌─────────────────────────────────────────────────┐
│                     Server                       │
│                                                  │
│  ┌──────────────┐         ┌──────────────┐      │
│  │   Backend    │         │   Consumer   │      │
│  │  (systemd)   │         │  (systemd)   │      │
│  │  Port: 8080  │         │  Port: 8081  │      │
│  └──────┬───────┘         └──────┬───────┘      │
│         │                        │              │
│         └────────┬───────────────┘              │
│                  │                              │
│         ┌────────▼────────┐                     │
│         │  Kafka (Docker) │                     │
│         │  Port: 9093     │                     │
│         └─────────────────┘                     │
└─────────────────────────────────────────────────┘
```

## 📦 서버 초기 설정

### 1. 사용자 및 디렉토리 생성

```bash
# eati 사용자 생성 (이미 있으면 스킵)
sudo useradd -r -s /bin/false eati

# 디렉토리 생성
sudo mkdir -p /srv/eati
sudo mkdir -p /srv/eati-consumer
sudo mkdir -p /etc/eati
sudo mkdir -p /home/opc/eati-deploy

# 권한 설정
sudo chown eati:eati /srv/eati
sudo chown eati:eati /srv/eati-consumer
sudo chown root:eati /etc/eati
sudo chmod 750 /etc/eati
```

### 2. 환경변수 파일 생성

#### 백엔드 환경변수 (`/etc/eati/eati.env`)

```bash
sudo nano /etc/eati/eati.env
```

내용:
```bash
# Database
JDBC_URL=jdbc:postgresql://YOUR_DB_HOST:5432/eati
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# Redis
SPRING_DATA_REDIS_HOST=YOUR_REDIS_HOST
SPRING_DATA_REDIS_PORT=6379

# JWT
JWT_SECRET=your-jwt-secret-key-min-256-bits

# Kafka (추가)
KAFKA_BOOTSTRAP_SERVERS=localhost:9093
```

권한 설정:
```bash
sudo chmod 640 /etc/eati/eati.env
sudo chown root:eati /etc/eati/eati.env
```

#### 컨슈머 환경변수 (`/etc/eati/eati-consumer.env`)

```bash
sudo nano /etc/eati/eati-consumer.env
```

내용:
```bash
# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9093

# MongoDB
MONGODB_URI=mongodb://YOUR_MONGODB_HOST:27017/eati

# Spring Profile
SPRING_PROFILES_ACTIVE=prod
```

권한 설정:
```bash
sudo chmod 640 /etc/eati/eati-consumer.env
sudo chown root:eati /etc/eati/eati-consumer.env
```

### 3. Kafka/Zookeeper Docker 설정

#### Docker 및 Docker Compose 설치

```bash
# Docker 설치 (이미 설치되어 있으면 스킵)
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker

# Docker Compose 설치
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### Kafka 시작

```bash
cd /home/opc/eati-deploy

# docker-compose.kafka.yml 파일이 있어야 함
docker-compose -f docker-compose.kafka.yml up -d

# 상태 확인
docker-compose -f docker-compose.kafka.yml ps

# 토픽 자동 생성 확인 (백엔드나 컨슈머 시작 후)
docker exec -it eati-kafka kafka-topics --list --bootstrap-server localhost:9092
```

### 4. Systemd 서비스 설정

#### 컨슈머 서비스 파일 설치

```bash
# 서비스 파일 복사 (git repo의 systemd/eati-consumer.service)
sudo cp systemd/eati-consumer.service /etc/systemd/system/

# 권한 설정
sudo chmod 644 /etc/systemd/system/eati-consumer.service

# systemd 리로드
sudo systemctl daemon-reload

# 서비스 활성화
sudo systemctl enable eati-consumer
```

#### 기존 백엔드 서비스 수정 (필요시)

기존 `eati.service` 파일에 Kafka Docker 의존성 추가:

```bash
sudo nano /etc/systemd/system/eati.service
```

`[Unit]` 섹션에 추가:
```ini
After=network.target docker.service
Wants=docker.service
```

수정 후:
```bash
sudo systemctl daemon-reload
```

## 🚀 배포 프로세스

### GitHub Actions 워크플로우 수정

기존 워크플로우를 유지하되, 컨슈머 JAR도 함께 배포하도록 수정:

새로운 워크플로우 파일 `.github/workflows/deploy-eati-systemd.yml`이 생성되었습니다.

**사용하려면**: 기존 `deploy-eati.yml`을 비활성화하거나 삭제하고 `deploy-eati-systemd.yml`을 사용하세요.

### 초회 배포 순서

서버에서 다음 순서로 설정:

```bash
# 1. Kafka Docker 시작
cd /home/opc/eati-deploy
docker-compose -f docker-compose.kafka.yml up -d

# 2. 백엔드 수동 배포 (처음 한 번)
# JAR 파일을 먼저 수동으로 올려놓기
sudo mv /home/opc/eati-deploy/backend.jar /srv/eati/app.jar
sudo chown eati:eati /srv/eati/app.jar
sudo systemctl start eati

# 3. 컨슈머 서비스 파일 설치
sudo cp /home/opc/eati-deploy/systemd/eati-consumer.service /etc/systemd/system/
sudo chmod 644 /etc/systemd/system/eati-consumer.service
sudo systemctl daemon-reload
sudo systemctl enable eati-consumer

# 4. 컨슈머 수동 배포 (처음 한 번)
sudo mv /home/opc/eati-deploy/consumer.jar /srv/eati-consumer/app.jar
sudo chown eati:eati /srv/eati-consumer/app.jar
sudo systemctl start eati-consumer

# 5. 상태 확인
sudo systemctl status eati
sudo systemctl status eati-consumer
docker ps
```

## 🔍 운영 및 모니터링

### 서비스 관리

```bash
# 백엔드
sudo systemctl start eati
sudo systemctl stop eati
sudo systemctl restart eati
sudo systemctl status eati
sudo journalctl -u eati -f

# 컨슈머
sudo systemctl start eati-consumer
sudo systemctl stop eati-consumer
sudo systemctl restart eati-consumer
sudo systemctl status eati-consumer
sudo journalctl -u eati-consumer -f

# Kafka
docker-compose -f docker-compose.kafka.yml up -d
docker-compose -f docker-compose.kafka.yml down
docker-compose -f docker-compose.kafka.yml logs -f
```

### 로그 확인

```bash
# 백엔드 로그
sudo journalctl -u eati -f --since "10 minutes ago"

# 컨슈머 로그
sudo journalctl -u eati-consumer -f --since "10 minutes ago"

# Kafka 로그
docker logs -f eati-kafka

# 모든 로그 함께 보기
sudo journalctl -u eati -u eati-consumer -f
```

### Kafka 토픽 및 메시지 확인

```bash
# 토픽 목록
docker exec -it eati-kafka kafka-topics --list --bootstrap-server localhost:9092

# eati-user-events 토픽 상세
docker exec -it eati-kafka kafka-topics --describe --topic eati-user-events --bootstrap-server localhost:9092

# 메시지 확인
docker exec -it eati-kafka kafka-console-consumer \
  --topic eati-user-events \
  --from-beginning \
  --bootstrap-server localhost:9092 \
  --max-messages 10

# Consumer Group 상태
docker exec -it eati-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --describe \
  --group eati-user-events-consumer
```

## 🐛 트러블슈팅

### 백엔드가 Kafka에 연결 못할 때

```bash
# Kafka 상태 확인
docker ps | grep kafka

# Kafka 로그 확인
docker logs eati-kafka

# 백엔드 환경변수 확인
sudo cat /etc/eati/eati.env | grep KAFKA

# 포트 확인
netstat -tlnp | grep 9093
```

### 컨슈머가 메시지를 받지 못할 때

```bash
# 컨슈머 로그 확인
sudo journalctl -u eati-consumer -f

# MongoDB 연결 확인
mongosh $MONGODB_URI

# Consumer Group Lag 확인
docker exec -it eati-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --describe \
  --group eati-user-events-consumer
```

### 메모리 부족

```bash
# 현재 메모리 사용량
free -h

# 서비스별 메모리 사용량
sudo systemctl status eati
sudo systemctl status eati-consumer
docker stats

# JVM 힙 크기 조정 (서비스 파일에서)
sudo nano /etc/systemd/system/eati-consumer.service
# Environment="JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC"
# 위 값을 조정 후
sudo systemctl daemon-reload
sudo systemctl restart eati-consumer
```

## 📊 헬스체크

```bash
# 백엔드 헬스
curl http://localhost:8080/actuator/health

# 컨슈머 헬스
curl http://localhost:8081/actuator/health

# Kafka 헬스
docker exec -it eati-kafka kafka-broker-api-versions --bootstrap-server localhost:9092
```

## 🔐 보안 체크리스트

- [ ] 환경변수 파일 권한 확인 (`640`, `root:eati`)
- [ ] Kafka 포트 방화벽 설정 (외부 접근 차단)
- [ ] MongoDB 접근 제어
- [ ] 서비스 사용자로 실행 (`eati`)
- [ ] SELinux 컨텍스트 확인 (`restorecon`)

## 📝 요약

### 필요한 환경변수

**백엔드 (`/etc/eati/eati.env`)**:
- `JDBC_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `SPRING_DATA_REDIS_HOST`
- `SPRING_DATA_REDIS_PORT`
- `JWT_SECRET`
- `KAFKA_BOOTSTRAP_SERVERS=localhost:9093` (신규)

**컨슈머 (`/etc/eati/eati-consumer.env`)**:
- `KAFKA_BOOTSTRAP_SERVERS=localhost:9093`
- `MONGODB_URI`
- `SPRING_PROFILES_ACTIVE=prod`

### 서비스 구성

1. **Kafka/Zookeeper**: Docker Compose (`docker-compose.kafka.yml`)
2. **Backend**: Systemd (`eati.service`)
3. **Consumer**: Systemd (`eati-consumer.service`)

### 배포 흐름

```
GitHub Push → GitHub Actions → JAR 빌드 → 서버 복사 → systemctl restart
```
