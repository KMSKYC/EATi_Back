package msyc.eati.domain.menu.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse
import msyc.eati.adapter.inbound.web.restaurantmenu.dto.RestaurantMenuResponse
import msyc.eati.adapter.outbound.persistence.menu.MenuRepository
import msyc.eati.adapter.outbound.persistence.menu.RestaurantMenuRepository
import msyc.eati.adapter.outbound.persistence.restaurant.RestaurantRepository
import msyc.eati.domain.menu.model.Menu
import msyc.eati.domain.menu.model.RestaurantMenu
import msyc.eati.domain.restaurant.model.Restaurant
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class RestaurantMenuService(
    private val restaurantMenuRepository: RestaurantMenuRepository,
    private val restaurantRepository: RestaurantRepository,
    private val menuRepository: MenuRepository
) {

    /**
     * 특정 메뉴를 판매하는 음식점 목록 조회
     */
    fun getRestaurantsByMenuId(menuId: String): List<RestaurantMenuResponse> {
        log.info { "메뉴 ID로 음식점 목록 조회: menuId=$menuId" }

        val restaurantMenus = restaurantMenuRepository.findByMenuId(menuId)

        if (restaurantMenus.isEmpty()) {
            return emptyList()
        }

        // 메뉴 정보 조회
        val menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)

        // 음식점 정보를 한 번에 조회
        val restaurantIds = restaurantMenus.map { it.restaurantId!! }.distinct()
        val restaurants = restaurantRepository.findAllById(restaurantIds)
            .associateBy { it.restaurantId }

        return restaurantMenus.map { restaurantMenu ->
            val restaurant = restaurants[restaurantMenu.restaurantId]
            restaurantMenu.toResponse(menu, restaurant)
        }
    }

    private fun RestaurantMenu.toResponse(menu: Menu?, restaurant: Restaurant?) = RestaurantMenuResponse(
        restaurantMenuId = this.restaurantMenuId!!,
        categoryId = this.categoryId!!,
        restaurantId = this.restaurantId!!,
        menuId = this.menuId!!,
        restaurantMenuName = this.restaurantMenuName,
        price = this.price.toString(),
        description = this.description,
        menu = menu?.toMenuResponse(),
        restaurant = restaurant?.toRestaurantResponse()
    )

    private fun Menu.toMenuResponse() = MenuResponse(
        menuId = this.menuId!!,
        categoryId = this.categoryId!!,
        menuName = this.menuName,
        description = this.description,
        imageUrl = this.imageUrl
    )

    private fun Restaurant.toRestaurantResponse() = RestaurantResponse(
        categoryId = this.categoryId!!,
        restaurantId = this.restaurantId!!,
        restaurantName = this.restaurantName,
        address = this.address,
        description = this.description
    )
}
