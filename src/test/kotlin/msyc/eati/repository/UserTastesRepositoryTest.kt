package msyc.eati.repository

import msyc.eati.domain.User
import msyc.eati.domain.UserTastes
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
@DisplayName("UserTastes Repository 테스트")
class UserTastesRepositoryTest @Autowired constructor(
    private val userTastesRepository: UserTastesRepository,
    private val userRepository: UserRepository
) {

    @Test
    @DisplayName("사용자 취향 생성 테스트")
    fun `사용자 취향 생성 테스트`() {
        val user = getOrCreateUser("tastes@example.com", "취향테스터")
        val userTastes = createTestUserTastes(user)

        val saved = userTastesRepository.save(userTastes)

        assertThat(saved.userId).isNotNull()
        assertThat(saved.preferredCategories).isEqualTo("한식,일식")
        assertThat(saved.dislikedCategories).isEqualTo("중식")
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("사용자 ID로 취향 조회")
    fun `사용자 ID로 취향 조회`() {
        val user = userRepository.findByEmail("tastes@example.com").get()
        val found = userTastesRepository.findByUserId(user.userId!!)

        assertThat(found).isPresent
        assertThat(found.get().preferredCategories).isEqualTo("한식,일식")
    }

    @Test
    @DisplayName("사용자 ID로 취향 삭제")
    fun `사용자 ID로 취향 삭제`() {
        val user = userRepository.findByEmail("tastes@example.com").get()
        userTastesRepository.deleteByUserId(user.userId!!)

        val found = userTastesRepository.findByUserId(user.userId!!)
        assertThat(found).isEmpty
    }

    private fun getOrCreateUser(email: String, nickname: String): User {
        return userRepository.findByEmail(email).orElseGet {
            userRepository.save(
                User(
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
            )
        }
    }

    private fun createTestUserTastes(user: User) = UserTastes(
        user = user,
        preferredCategories = "한식,일식",
        dislikedCategories = "중식",
        recentMenus = "김치찌개,라멘",
        menuRestrictions = "땅콩",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
