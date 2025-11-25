package msyc.eati.repository

import msyc.eati.domain.Category
import msyc.eati.domain.Restaurant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Restaurant Repository 테스트")
class RestaurantRepositoryTest @Autowired constructor(
    private val restaurantRepository: RestaurantRepository
) {

    @Test
    @DisplayName("레스토랑 생성 테스트")
    fun `레스토랑 생성 테스트`() {
        val category = Category(
            name = "양식",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).apply { prePersist() }

        val restaurant = createTestRestaurant("테스트 레스토랑", category.categoryId!!)

        val saved = restaurantRepository.save(restaurant)

        assertThat(saved.restaurantId).isNotNull()
        assertThat(saved.name).isEqualTo("테스트 레스토랑")
        assertThat(saved.categoryId).isEqualTo(category.categoryId)
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    @DisplayName("이름으로 레스토랑 조회")
    fun `이름으로 레스토랑 조회`() {
        val found = restaurantRepository.findByName("테스트 레스토랑")

        assertThat(found).isPresent
        assertThat(found.get().name).isEqualTo("테스트 레스토랑")
    }

    @Test
    @DisplayName("이름으로 레스토랑 삭제")
    fun `이름으로 레스토랑 삭제`() {
        restaurantRepository.deleteByName("테스트 레스토랑")

        val found = restaurantRepository.findByName("테스트 레스토랑")
        assertThat(found).isEmpty
    }

    private fun createTestRestaurant(name: String, categoryId: String) = Restaurant(
        name = name,
        categoryId = categoryId,
        address = "서울시 강남구 테스트동 123",
        description = "테스트 레스토랑 설명",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
