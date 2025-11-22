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
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository
) {

    @Test
    @DisplayName("레스토랑 메뉴 생성 테스트")
    fun `레스토랑 메뉴 생성 테스트`() {
        val category = getOrCreateCategory("중식")
        val restaurant = getOrCreateRestaurant("테스트 중식당", category)
        val menu = createTestRestaurantMenu("짜장면", restaurant, category)

        val saved = restaurantMenuRepository.save(menu)

        assertThat(saved.menuId).isNotNull()
        assertThat(saved.name).isEqualTo("짜장면")
        assertThat(saved.restaurant.name).isEqualTo("테스트 중식당")
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

    private fun getOrCreateRestaurant(name: String, category: Category): Restaurant {
        return restaurantRepository.findByName(name).orElseGet {
            restaurantRepository.save(
                Restaurant(
                    name = name,
                    category = category,
                    address = "서울시 테스트구",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }

    private fun createTestRestaurantMenu(name: String, restaurant: Restaurant, category: Category) = RestaurantMenu(
        name = name,
        restaurant = restaurant,
        category = category,
        price = BigDecimal("8000.00"),
        description = "테스트 메뉴 설명",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
