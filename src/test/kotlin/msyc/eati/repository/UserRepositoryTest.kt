package msyc.eati.repository

import msyc.eati.adapter.out.persistence.user.UserRepository
import msyc.eati.domain.user.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Repository 테스트")
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
) {

    @Test
    @DisplayName("사용자 생성 테스트")
    fun `사용자 생성 테스트`() {
        val user = createTestUser("create@example.com", "새사용자")

        val saved = userRepository.save(user)

        assertThat(saved.userId).isNotNull()
        assertThat(saved.email).isEqualTo("create@example.com")
        assertThat(saved.nickname).isEqualTo("새사용자")
        assertThat(saved.status).isEqualTo("ACTIVE")
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("이메일로 사용자 조회")
    fun `이메일로 사용자 조회`() {
        val found = userRepository.findByEmail("create@example.com")

        assertThat(found).isPresent
        assertThat(found.get().nickname).isEqualTo("새사용자")
    }

    @Test
    @DisplayName("이메일로 사용자 삭제")
    fun `이메일로 사용자 삭제`() {
        userRepository.deleteByEmail("create@example.com")

        val found = userRepository.findByEmail("create@example.com")
        assertThat(found).isEmpty
    }

    private fun createTestUser(email: String, nickname: String) = User(
        email = email,
        password = "password123",
        status = "ACTIVE",
        nickname = nickname,
        birthdate = LocalDate.of(1990, 1, 1),
        gender = "M",
        region = "서울",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
