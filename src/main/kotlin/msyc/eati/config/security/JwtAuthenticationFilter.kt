package msyc.eati.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 인증 필터
 * - 모든 HTTP 요청에서 JWT 토큰을 검증
 * - 유효한 토큰이면 SecurityContext에 인증 정보 저장
 * - OncePerRequestFilter: 요청당 한 번만 실행되는 필터
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    /**
     * 필터의 핵심 로직
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // 요청에서 JWT 토큰 추출
            val token = getJwtFromRequest(request)

            // 토큰이 존재하고 유효한 경우
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰에서 인증 정보 생성
                val authentication = jwtTokenProvider.getAuthentication(token)
                // SecurityContext에 인증 정보 저장 (이후 @AuthenticationPrincipal로 접근 가능)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Security context에서 사용자 인증을 설정할 수 없습니다", e)
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response)
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰 추출
     */
    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 제거
            bearerToken.substring(7)
        } else null
    }
}
