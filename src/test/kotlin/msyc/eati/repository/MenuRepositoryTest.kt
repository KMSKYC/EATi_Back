package msyc.eati.repository

import msyc.eati.domain.Category
import msyc.eati.domain.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Menu Repository 테스트")
class MenuRepositoryTest @Autowired constructor(
    private val menuRepository: MenuRepository,
    private val categoryRepository: CategoryRepository
) {

    @Test
    @DisplayName("메뉴 생성 테스트")
    fun `메뉴 생성 테스트`() {
        val category = getOrCreateCategory("한식")
        val menu = createTestMenu("김치찌개", category)

        val saved = menuRepository.save(menu)

        assertThat(saved.menuId).isNotNull()
        assertThat(saved.name).isEqualTo("김치찌개")
        assertThat(saved.category.name).isEqualTo("한식")
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("이름으로 메뉴 조회")
    fun `이름으로 메뉴 조회`() {
        val found = menuRepository.findByName("김치찌개")

        assertThat(found).isPresent
        assertThat(found.get().name).isEqualTo("김치찌개")
    }

    @Test
    @DisplayName("이름으로 메뉴 삭제")
    fun `이름으로 메뉴 삭제`() {
        menuRepository.deleteByName("김치찌개")

        val found = menuRepository.findByName("김치찌개")
        assertThat(found).isEmpty
    }

    private fun getOrCreateCategory(name: String): Category {
        return categoryRepository.findByName(name).orElseGet {
            categoryRepository.save(
                Category(
                    name = name,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }

    private fun createTestMenu(name: String, category: Category) = Menu(
        name = name,
        category = category,
        description = "테스트 메뉴 설명",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
