package msyc.eati.repository

import msyc.eati.domain.Category
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Category Repository 테스트")
class CategoryRepositoryTest @Autowired constructor(
    private val categoryRepository: CategoryRepository
) {

    @Test
    @DisplayName("카테고리 생성 테스트")
    fun `카테고리 생성 테스트`() {
        val category = createTestCategory("일식")

        val saved = categoryRepository.save(category)

        assertThat(saved.categoryId).isNotNull()
        assertThat(saved.name).isEqualTo("일식")
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("이름으로 카테고리 조회")
    fun `이름으로 카테고리 조회`() {
        val found = categoryRepository.findByName("일식")

        assertThat(found).isPresent
        assertThat(found.get().name).isEqualTo("일식")
    }

    @Test
    @DisplayName("이름으로 카테고리 삭제")
    fun `이름으로 카테고리 삭제`() {
        categoryRepository.deleteByName("일식")

        val found = categoryRepository.findByName("일식")
        assertThat(found).isEmpty
    }

    private fun createTestCategory(name: String) = Category(
        name = name,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
