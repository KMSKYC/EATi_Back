package msyc.eati.repository

import msyc.eati.adapter.out.persistence.menu.CategoryRepository
import msyc.eati.adapter.out.persistence.menu.MenuRepository
import msyc.eati.domain.menu.model.Category
import msyc.eati.domain.menu.model.Menu
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
        val category = Category(
            name = "한식",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).apply { prePersist() }

        val menu = createTestMenu("김치찌개", category.categoryId!!)

        val saved = menuRepository.save(menu)

        assertThat(saved.menuId).isNotNull()
        assertThat(saved.name).isEqualTo("김치찌개")
        assertThat(saved.categoryId).isEqualTo(category.categoryId)
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

    private fun createTestMenu(name: String, categoryId: String) = Menu(
        name = name,
        categoryId = categoryId,
        description = "테스트 메뉴 설명",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
