package msyc.eati.adapter.inbound.web.menu

import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.adapter.inbound.web.restaurantmenu.dto.RestaurantMenuResponse
import msyc.eati.domain.menu.service.MenuService
import msyc.eati.domain.menu.service.RestaurantMenuService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/menus")
class MenuController(
    private val menuService: MenuService,
    private val restaurantMenuService: RestaurantMenuService
) {

    /**
     * 메뉴 목록 조회
     * @param categoryId 카테고리 ID (optional)
     */
    @GetMapping
    fun getMenus(
        @RequestParam(required = false) categoryId: String?
    ): ResponseEntity<List<MenuResponse>> {
        val menus = if (categoryId != null) {
            menuService.getMenusByCategory(categoryId)
        } else {
            menuService.getAllMenus()
        }
        return ResponseEntity.ok(menus)
    }

    /**
     * 메뉴 단건 조회
     */
    @GetMapping("/{menuId}")
    fun getMenu(
        @PathVariable menuId: String
    ): ResponseEntity<MenuResponse> {
        val menu = menuService.getMenu(menuId)
        return ResponseEntity.ok(menu)
    }

    /**
     * 메뉴 랜덤 조회
     */
    @GetMapping("random")
    fun getRandomMenu(): ResponseEntity<MenuResponse> {
        val menu = menuService.getRandomMenu()
        return ResponseEntity.ok(menu)
    }

    /**
     * 특정 메뉴를 판매하는 맛집 목록 조회
     */
    @GetMapping("/{menuId}/restaurants")
    fun getRestaurantsByMenuId(
        @PathVariable menuId: String
    ): ResponseEntity<List<RestaurantMenuResponse>> {
        val restaurants = restaurantMenuService.getRestaurantsByMenuId(menuId)
        return ResponseEntity.ok(restaurants)
    }
}
