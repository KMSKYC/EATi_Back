package msyc.eati.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Spring Security 설정
 * - JWT 기반 인증 설정
 * - CSRF 비활성화 (REST API이므로)
 * - Stateless 세션 관리
 * - 권한별 접근 제어
 */
@Configuration
@EnableWebSecurity  // Spring Security 활성화
@EnableMethodSecurity  // 메서드 레벨 보안 활성화 (@PreAuthorize, @Secured 등 사용 가능)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customUserDetailsService: CustomUserDetailsService
) {

    /**
     * 비밀번호 암호화 인코더
     * BCrypt 해싱 알고리즘 사용 (단방향 암호화)
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 인증 관리자
     * 로그인 시 사용자 인증을 처리
     */
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    /**
     * Security 필터 체인 설정
     * HTTP 보안 설정의 핵심
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            // CORS 설정 (Spring Security 레벨)
            .cors { }

            // CSRF 보호 비활성화 (REST API는 stateless이므로 불필요)
            .csrf { csrf -> csrf.disable() }

            // 세션 사용 안 함 (JWT 토큰 기반 인증)
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // formLogin, httpBasic 비활성화
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

            // URL별 접근 권한 설정
            .authorizeHttpRequests { authorize ->
                authorize
                    // 인증 없이 접근 가능한 경로
                    .requestMatchers(
                        "/", // 메인화면
                        "/index.html", // 인덱스 페이지
                        "/api/auth/**", // 회원가입, 로그인
                        "/api/categories/**", // 카테고리 조회
                        "/api/menus/**", // 메뉴 조회
                        "/actuator/**", // 헬스체크
                        "/error", // 에러 페이지
                        "/static/**", // 정적 리소스
                        "/css/**", // CSS 파일
                        "/js/**", // JavaScript 파일
                        "/images/**" // 이미지 파일
                    ).permitAll()
                    // 관리자 전용 경로
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    // 그 외 모든 요청은 인증 필요
                    .anyRequest().authenticated()
            }

            // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

            .build()
    }
}
