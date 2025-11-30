package msyc.eati.adapter.inbound.web.auth

import jakarta.validation.Valid
import msyc.eati.adapter.inbound.web.auth.dto.LoginRequest
import msyc.eati.adapter.inbound.web.auth.dto.SignUpRequest
import msyc.eati.adapter.inbound.web.auth.dto.TokenResponse
import msyc.eati.adapter.inbound.web.auth.dto.UserResponse
import msyc.eati.domain.user.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

/**
 * 인증 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<UserResponse> {
        val userResponse = authService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.login(request)
        return ResponseEntity.ok(tokenResponse)
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    fun getCurrentUser(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<UserResponse> {
        val userResponse = authService.getCurrentUser(userDetails.username)
        return ResponseEntity.ok(userResponse)
    }
}
