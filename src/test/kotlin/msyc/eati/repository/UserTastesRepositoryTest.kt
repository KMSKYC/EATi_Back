package msyc.eati.repository

import msyc.eati.adapter.out.persistence.user.UserTastesRepository
import msyc.eati.domain.user.model.User
import msyc.eati.domain.user.model.UserTastes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserTastes Repository 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserTastesRepositoryTest @Autowired constructor(
    private val userTastesRepository: UserTastesRepository
) {
    private lateinit var testUserId: String

    @Test
    @DisplayName("사용자 취향 생성 테스트")
    fun `사용자 취향 생성 테스트`() {
        val user = User(
            userId = "pjuElFgHhO",
            email = "tastes@example.com",
            password = "password123",
            status = "ACTIVE",
            nickname = "취향테스터",
            birthdate = LocalDate.of(1990, 1, 1),
            gender = "M",
            region = "서울",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).apply { prePersist() }

        testUserId = user.userId!!
        val userTastes = createTestUserTastes(testUserId)
        val saved = userTastesRepository.save(userTastes)

        assertThat(saved.userId).isNotNull()
    }

    @Test
    @DisplayName("데이터 존재 여부 및 필드 비교")
    fun `데이터 존재 여부 및 필드 비교`() {
        val found = userTastesRepository.findByUserId("pjuElFgHhO")

        assertThat(found).isPresent
        assertThat(found.get().disLikedCategoryIds).containsExactly("중식")
        assertThat(found.get().recentMenus).containsExactly("김치찌개", "라멘")
    }

    @Test
    @DisplayName("모든 데이터 삭제")
    fun `모든 데이터 삭제`() {
        userTastesRepository.deleteByUserId("pjuElFgHhO")

        val found = userTastesRepository.findByUserId("pjuElFgHhO")
        assertThat(found).isEmpty
    }

    private fun createTestUserTastes(userId: String) = UserTastes(
        userId = userId,
        likedCategoryIds = listOf("한식", "일식"),
        disLikedCategoryIds = listOf("중식"),
        recentMenus = listOf("김치찌개", "라멘"),
        menuRestrictions = listOf("땅콩"),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
