package msyc.eati.repository

import msyc.eati.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.OffsetDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Repository 테스트")
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
) {

    @Test
    @DisplayName("사용자 CRUD 테스트")
    fun `user crud operations should work`() {
        // Create
        val user = createTestUser("test@example.com", "테스터")
        val saved = userRepository.save(user)
        assertThat(saved.userId).isNotNull()
        assertThat(saved.email).isEqualTo("test@example.com")

        // Read
        val found = userRepository.findById(saved.userId!!).get()
        assertThat(found.nickname).isEqualTo("테스터")

        // Update
        found.nickname = "수정된이름"
        val updated = userRepository.save(found)
        assertThat(updated.nickname).isEqualTo("수정된이름")

        // Delete
        userRepository.deleteById(saved.userId!!)
        assertThat(userRepository.findById(saved.userId!!)).isEmpty
    }

    @Test
    @DisplayName("사용자 생성 테스트")
    fun `should create user successfully`() {
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
    fun `should find user by email`() {
        val user = createTestUser("find@example.com", "찾을사용자")
        userRepository.save(user)

        val found = userRepository.findByEmail("find@example.com")

        assertThat(found).isPresent
        assertThat(found.get().nickname).isEqualTo("찾을사용자")
    }

    private fun createTestUser(email: String, nickname: String) = User(
        email = email,
        password = "password123",
        status = "ACTIVE",
        nickname = nickname,
        birthdate = LocalDate.of(1990, 1, 1),
        gender = "M",
        region = "서울",
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )
}
