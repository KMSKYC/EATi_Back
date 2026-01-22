package msyc.eati.domain.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.adapter.inbound.web.auth.dto.UserResponse
import msyc.eati.adapter.outbound.persistence.user.UserRepository
import msyc.eati.domain.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * 전체 회원 목록 조회
     */
    fun getAllUsers(): List<UserResponse> {
        log.info { "전체 회원 목록 조회" }
        return userRepository.findAllByDeletedAtIsNull()
            .map { it.toResponse() }
    }

    /**
     * 회원 단건 조회
     */
    fun getUser(userId: String): UserResponse {
        log.info { "회원 단건 조회: userId=$userId" }
        val user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
            ?: throw IllegalArgumentException("회원을 찾을 수 없습니다: $userId")
        return user.toResponse()
    }

    private fun User.toResponse() = UserResponse(
        userId = this.userId!!,
        email = this.email,
        nickname = this.nickname,
        birthdate = this.birthdate,
        gender = this.gender,
        region = this.region,
        role = this.userRole
    )
}
