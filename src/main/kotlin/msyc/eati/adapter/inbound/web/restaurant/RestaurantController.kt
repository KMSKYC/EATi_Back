package msyc.eati.adapter.inbound.web.restaurant

import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse
import msyc.eati.domain.menu.service.RestaurantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantService,
) {

    /**
     * 레스토랑 목록 조회
     * @param restaurantId 레스토랑 ID (optional)
     */
    @GetMapping
    fun getRestaurant(
        @RequestParam(required = false) restaurantId: String?
    ): ResponseEntity<List<RestaurantResponse>> {
        val restaurant = if (restaurantId != null) {
            null
        } else {
            null
        }
        return ResponseEntity.ok(restaurant)
    }

}
