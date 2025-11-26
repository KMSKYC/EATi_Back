### 🏗️ 디렉토리 구조
```
src/main/kotlin/msyc/eati
├── domain          // 1. 핵심 비즈니스 로직 (가장 안쪽 원)
│   ├── model       // 핵심 도메인 객체 (Entity, Value Object)
│   └── port        // Application 계층이 의존하는 인터페이스 (Input/Output Port)
│       ├── in      // Application Layer로의 입력 포트 (Service Interface)
│       └── out     // 외부 인프라로의 출력 포트 (Persistence Interface, Gateway)
|
├── application     // 2. 유스케이스 구현 (도메인 포트 구현)
│   └── service     // Business Logic 구현 (Port/in 구현체, Interactor)
│       └── impl    // 서비스 인터페이스의 실제 구현체
|
├── adapter         // 3. 외부 기술 어댑터 (바깥쪽 원)
│   ├── in          // 외부 요청을 Domain/Application으로 변환 (Driving Adapter)
│   │   ├── web     // Spring Web (Controller) - Port/in 호출
│   │   └── message // Message Queue Listener 등
│   └── out         // Domain/Application의 요청을 외부 인프라로 변환 (Driven Adapter)
│       ├── persistence // Spring Data JPA, MyBatis 등 (Port/out 구현)
│       │   ├── entity  // DB 테이블 매핑용 JPA Entity (Domain Model과 분리)
│       │   └── repository // Spring Data Repository Interface (Port/out 구현)
│       └── external  // 외부 API 호출 클라이언트 (HTTP Client, OAuth 등)
|
└── common          // 4. 공통 기능 및 설정 (Frameworks & Drivers)
    ├── config      // Spring 설정 클래스 (SecurityConfig, WebConfig 등)
    ├── exception   // 사용자 정의 예외 클래스
    ├── util        // 공통 유틸리티 클래스
    └── dto         // DTO (Data Transfer Object) - 계층 간 데이터 전송용
        ├── request   // Controller로 들어오는 요청 DTO
        └── response  // Controller가 반환하는 응답 DTO
```