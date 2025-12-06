package msyc.eati.config.security

import msyc.eati.adapter.outbound.persistence.user.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Spring Security의 UserDetailsService 구현
 * - 로그인 시 사용자 정보를 DB에서 조회
 * - 사용자 정보를 Spring Security의 UserDetails 형식으로 변환
 */
@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    /**
     * 이메일로 사용자 정보 조회
     */
    override fun loadUserByUsername(email: String): UserDetails {
        // DB에서 사용자 조회
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다: $email") }

        // 권한 정보 생성 (ROLE_ 접두사 필요)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.userRole.name}"))

        // Spring Security의 UserDetails 객체 생성
        return User.builder()
            .username(user.email) // 이메일을 username으로 사용
            .password(user.userPassword) // 암호화된 비밀번호
            .authorities(authorities) // 권한 목록
            .accountExpired(false) // 계정 만료 여부
            .accountLocked(user.status == "LOCKED") // 계정 잠김 여부
            .credentialsExpired(false) // 비밀번호 만료 여부
            .disabled(user.status == "INACTIVE" || user.deletedAt != null) // 계정 비활성화 여부
            .build()
    }
}
