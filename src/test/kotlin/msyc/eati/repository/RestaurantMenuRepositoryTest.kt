package msyc.eati.repository

import msyc.eati.domain.Category
import msyc.eati.domain.Restaurant
import msyc.eati.domain.RestaurantMenu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("RestaurantMenu Repository 테스트")
class RestaurantMenuRepositoryTest @Autowired constructor(
    private val restaurantMenuRepository: RestaurantMenuRepository,
) {

    @Test
    @DisplayName("레스토랑 메뉴 생성 테스트")
    fun `레스토랑 메뉴 생성 테스트`() {
        val category = Category(
            name = "중식",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).apply { prePersist() }

        val restaurant = Restaurant(
            name = "테스트 중식당",
            categoryId = category.categoryId,
            address = "서울시 테스트구",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).apply { prePersist() }

        val menu = createTestRestaurantMenu("짜장면", restaurant.restaurantId!!, category.categoryId!!)

        val saved = restaurantMenuRepository.save(menu)

        assertThat(saved.menuId).isNotNull()
        assertThat(saved.name).isEqualTo("짜장면")
        assertThat(saved.restaurantId).isEqualTo(restaurant.restaurantId)
        assertThat(saved.categoryId).isEqualTo(category.categoryId)
        assertThat(saved.price).isEqualTo(BigDecimal("8000.00"))
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("이름으로 레스토랑 메뉴 조회")
    fun `이름으로 레스토랑 메뉴 조회`() {
        val found = restaurantMenuRepository.findByName("짜장면")

        assertThat(found).isPresent
        assertThat(found.get().name).isEqualTo("짜장면")
    }

    @Test
    @DisplayName("이름으로 레스토랑 메뉴 삭제")
    fun `이름으로 레스토랑 메뉴 삭제`() {
        restaurantMenuRepository.deleteByName("짜장면")

        val found = restaurantMenuRepository.findByName("짜장면")
        assertThat(found).isEmpty
    }

    private fun createTestRestaurantMenu(name: String, restaurantId: String, categoryId: String) = RestaurantMenu(
        name = name,
        restaurantId = restaurantId,
        categoryId = categoryId,
        price = BigDecimal("8000.00"),
        description = "테스트 메뉴 설명",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
