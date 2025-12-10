package msyc.eati.domain.user.service

import msyc.eati.adapter.inbound.web.auth.dto.EmailCheckResponse
import msyc.eati.adapter.inbound.web.auth.dto.LoginRequest
import msyc.eati.adapter.inbound.web.auth.dto.SignUpRequest
import msyc.eati.adapter.inbound.web.auth.dto.TokenResponse
import msyc.eati.adapter.inbound.web.auth.dto.UserResponse
import msyc.eati.adapter.outbound.persistence.user.UserRepository
import msyc.eati.config.security.JwtTokenProvider
import msyc.eati.domain.user.model.User
import msyc.eati.domain.user.model.UserRole
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 * - 회원가입
 * - 로그인
 * - 사용자 정보 조회
 */
@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager
) {

    /**
     * 회원가입
     */
    fun signUp(request: SignUpRequest): UserResponse {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다")
        }

        // User 엔티티 생성
        val user = User(
            email = request.email,
            userPassword = passwordEncoder.encode(request.password),  // BCrypt로 비밀번호 암호화
            nickname = request.nickname,
            birthdate = request.birthdate,
            gender = request.gender,
            region = request.region,
            userRole = UserRole.USER,   // 기본 권한은 USER
            status = "ACTIVE"       // 기본 상태는 ACTIVE
        )

        // DB에 저장
        val savedUser = userRepository.save(user)

        // DTO로 변환하여 반환
        return UserResponse(
            userId = savedUser.userId!!,
            email = savedUser.email,
            nickname = savedUser.nickname,
            birthdate = savedUser.birthdate,
            gender = savedUser.gender,
            region = savedUser.region,
            role = savedUser.userRole
        )
    }

    /**
     * 로그인
     */
    fun login(request: LoginRequest): TokenResponse {
        // 인증 수행 (비밀번호 검증, 실패 시 AuthenticationException 발생)
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        // 사용자 정보 조회
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        // 마지막 로그인 시간 업데이트
        user.lastLoginAt = OffsetDateTime.now()
        userRepository.save(user)

        // JWT 토큰 생성
        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = user.userId!!,
            email = user.email,
            role = user.userRole.name
        )

        val refreshToken = jwtTokenProvider.generateRefreshToken(user.userId!!)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    fun getCurrentUser(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        return UserResponse(
            userId = user.userId!!,
            email = user.email,
            nickname = user.nickname,
            birthdate = user.birthdate,
            gender = user.gender,
            region = user.region,
            role = user.userRole
        )
    }

    /**
     * 이메일 중복 확인
     * 회원가입 전 이메일 사용 가능 여부를 확인
     */
    @Transactional(readOnly = true)
    fun checkEmailAvailability(email: String): EmailCheckResponse {
        val exists = userRepository.existsByEmail(email)

        return EmailCheckResponse(
            email = email,
            available = !exists,
            message = if (exists) "이미 사용 중인 이메일입니다" else "사용 가능한 이메일입니다"
        )
    }
}
