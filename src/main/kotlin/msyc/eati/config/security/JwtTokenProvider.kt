package msyc.eati.config.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * JWT 토큰 생성 및 검증을 담당하는 Provider
 * - Access Token과 Refresh Token 생성
 * - 토큰 검증 및 파싱
 * - 토큰에서 인증 정보 추출
 */
@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    // JWT 서명에 사용할 비밀키 (HMAC-SHA 알고리즘)
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    /**
     * Access Token 생성
     */
    fun generateAccessToken(userId: String, email: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .subject(userId) // 토큰 주체 (사용자 ID)
            .claim("email", email) // 커스텀 클레임: 이메일
            .claim("role", role) // 커스텀 클레임: 권한
            .claim("type", "access") // 토큰 타입
            .issuedAt(now) // 발급 시간
            .expiration(expiryDate) // 만료 시간
            .signWith(key) // 서명
            .compact()
    }

    /**
     * Refresh Token 생성
     */
    fun generateRefreshToken(userId: String): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .subject(userId)
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    /**
     * 토큰에서 사용자 ID 추출
     */
    fun getUserIdFromToken(token: String): String {
        val claims = parseClaims(token)
        return claims.subject
    }

    /**
     * JWT 토큰으로부터 Spring Security Authentication 객체 생성
     */
    fun getAuthentication(token: String): Authentication {
        val claims = parseClaims(token)
        val email = claims["email"] as String
        val role = claims["role"] as String

        // 권한 정보 생성 (ROLE_ 접두사 필요)
        val authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_$role"))

        // UserDetails 객체 생성
        val principal = User(email, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    /**
     * JWT 토큰 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (e: SecurityException) {
            false  // 잘못된 서명
        } catch (e: MalformedJwtException) {
            false  // 잘못된 토큰 형식
        } catch (e: ExpiredJwtException) {
            false  // 만료된 토큰
        } catch (e: UnsupportedJwtException) {
            false  // 지원하지 않는 토큰
        } catch (e: IllegalArgumentException) {
            false  // 빈 토큰
        }
    }

    /**
     * JWT 토큰 파싱하여 Claims 추출
     */
    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)  // 서명 검증
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
