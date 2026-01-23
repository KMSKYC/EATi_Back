package msyc.eati.adapter.inbound.web.admin.restaurantmenu

import jakarta.validation.Valid
import msyc.eati.adapter.inbound.web.admin.restaurantmenu.dto.RestaurantMenuCreateRequest
import msyc.eati.adapter.inbound.web.admin.restaurantmenu.dto.RestaurantMenuUpdateRequest
import msyc.eati.adapter.inbound.web.restaurantmenu.dto.RestaurantMenuResponse
import msyc.eati.domain.menu.service.RestaurantMenuService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/restaurant-menus")
class AdminRestaurantMenuController(
    private val restaurantMenuService: RestaurantMenuService
) {

    /**
     * 레스토랑 메뉴 등록
     */
    @PostMapping
    fun createRestaurantMenu(
        @Valid @RequestBody request: RestaurantMenuCreateRequest
    ): ResponseEntity<RestaurantMenuResponse> {
        val restaurantMenu = restaurantMenuService.createRestaurantMenu(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantMenu)
    }

    /**
     * 레스토랑 메뉴 수정
     */
    @PutMapping("/{restaurantMenuId}")
    fun updateRestaurantMenu(
        @PathVariable restaurantMenuId: String,
        @Valid @RequestBody request: RestaurantMenuUpdateRequest
    ): ResponseEntity<RestaurantMenuResponse> {
        val restaurantMenu = restaurantMenuService.updateRestaurantMenu(restaurantMenuId, request)
        return ResponseEntity.ok(restaurantMenu)
    }

    /**
     * 레스토랑 메뉴 삭제
     */
    @DeleteMapping("/{restaurantMenuId}")
    fun deleteRestaurantMenu(
        @PathVariable restaurantMenuId: String
    ): ResponseEntity<Void> {
        restaurantMenuService.deleteRestaurantMenu(restaurantMenuId)
        return ResponseEntity.noContent().build()
    }
}
