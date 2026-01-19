package msyc.eati.adapter.inbound.web.restaurantmenu

import msyc.eati.adapter.inbound.web.restaurantmenu.dto.RestaurantMenuResponse
import msyc.eati.domain.menu.service.RestaurantMenuService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurant-menu")
class RestaurantMenuController(
    private val restaurantMenuService: RestaurantMenuService,
) {

    /**
     * 특정 메뉴를 판매하는 음식점 목록 조회
     * @param menuId 메뉴 ID
     */
    @GetMapping
    fun getRestaurantsByMenu(
        @RequestParam menuId: String
    ): ResponseEntity<List<RestaurantMenuResponse>> {
        val restaurants = restaurantMenuService.getRestaurantsByMenuId(menuId)
        return ResponseEntity.ok(restaurants)
    }

}
