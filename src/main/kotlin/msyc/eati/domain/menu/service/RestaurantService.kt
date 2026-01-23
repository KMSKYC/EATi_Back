package msyc.eati.domain.menu.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.adapter.inbound.web.admin.restaurant.dto.RestaurantCreateRequest
import msyc.eati.adapter.inbound.web.admin.restaurant.dto.RestaurantUpdateRequest
import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse
import msyc.eati.adapter.outbound.persistence.menu.MenuRepository
import msyc.eati.adapter.outbound.persistence.restaurant.RestaurantRepository
import msyc.eati.domain.menu.model.Menu
import msyc.eati.domain.restaurant.model.Restaurant
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class RestaurantService(
    private val menuRepository: MenuRepository,
    private val restaurantRepository: RestaurantRepository
) {

    /**
     * 전체 메뉴 목록 조회
     */
    fun getAllMenus(): List<MenuResponse> {
        log.info { "전체 메뉴 목록 조회" }
        return menuRepository.findAllByDeletedAtIsNull()
            .map { it.toResponse() }
    }

    /**
     * 카테고리별 메뉴 목록 조회
     */
    fun getMenusByCategory(categoryId: String): List<MenuResponse> {
        log.info { "카테고리별 메뉴 조회: categoryId=$categoryId" }
        return menuRepository.findAllByCategoryIdAndDeletedAtIsNull(categoryId)
            .map { it.toResponse() }
    }

    /**
     * 메뉴 단건 조회
     */
    fun getMenu(menuId: String): MenuResponse {
        val menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)
            ?: throw IllegalArgumentException("메뉴를 찾을 수 없습니다: $menuId")
        return menu.toResponse()
    }

    /**
     * 랜덤 메뉴 조회
     */
    fun getRandomMenu(): MenuResponse {
        val menu = menuRepository.findRandomMenu()
            ?: throw IllegalArgumentException("조회 가능한 메뉴가 없습니다")
        return menu.toResponse()
    }

    private fun Menu.toResponse() = MenuResponse(
        menuId = this.menuId!!,
        categoryId = this.categoryId!!,
        menuName = this.menuName ?: "",
        description = this.description,
        imageUrl = this.imageUrl
    )

    private fun Restaurant.toResponse() = RestaurantResponse(
        restaurantId = this.restaurantId!!,
        categoryId = this.categoryId!!,
        restaurantName = this.restaurantName,
        address = this.address,
        description = this.description
    )

    /**
     * 전체 레스토랑 목록 조회
     */
    fun getAllRestaurants(): List<RestaurantResponse> {
        log.info { "전체 레스토랑 목록 조회" }
        return restaurantRepository.findAllByDeletedAtIsNull()
            .map { it.toResponse() }
    }

    /**
     * 레스토랑 단건 조회
     */
    fun getRestaurant(restaurantId: String): RestaurantResponse {
        log.info { "레스토랑 단건 조회: restaurantId=$restaurantId" }
        val restaurant = restaurantRepository.findByRestaurantIdAndDeletedAtIsNull(restaurantId)
            ?: throw IllegalArgumentException("레스토랑을 찾을 수 없습니다: $restaurantId")
        return restaurant.toResponse()
    }

    /**
     * 레스토랑 생성
     */
    @Transactional
    fun createRestaurant(request: RestaurantCreateRequest): RestaurantResponse {
        log.info { "레스토랑 생성: ${request.restaurantName}" }

        val restaurant = Restaurant(
            categoryId = request.categoryId,
            restaurantName = request.restaurantName,
            address = request.address,
            description = request.description,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedRestaurant = restaurantRepository.save(restaurant)
        return savedRestaurant.toResponse()
    }

    /**
     * 레스토랑 수정
     */
    @Transactional
    fun updateRestaurant(restaurantId: String, request: RestaurantUpdateRequest): RestaurantResponse {
        log.info { "레스토랑 수정: restaurantId=$restaurantId" }

        val restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow { IllegalArgumentException("레스토랑을 찾을 수 없습니다: $restaurantId") }

        if (restaurant.deletedAt != null) {
            throw IllegalArgumentException("삭제된 레스토랑입니다: $restaurantId")
        }

        restaurant.categoryId = request.categoryId
        restaurant.restaurantName = request.restaurantName
        restaurant.address = request.address
        restaurant.description = request.description

        return restaurant.toResponse()
    }

    /**
     * 레스토랑 삭제 (soft delete)
     */
    @Transactional
    fun deleteRestaurant(restaurantId: String) {
        log.info { "레스토랑 삭제: restaurantId=$restaurantId" }

        val restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow { IllegalArgumentException("레스토랑을 찾을 수 없습니다: $restaurantId") }

        if (restaurant.deletedAt != null) {
            throw IllegalArgumentException("이미 삭제된 레스토랑입니다: $restaurantId")
        }

        restaurant.deletedAt = LocalDateTime.now()
    }
}
