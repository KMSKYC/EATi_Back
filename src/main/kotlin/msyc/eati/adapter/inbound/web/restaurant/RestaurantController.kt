package msyc.eati.adapter.inbound.web.restaurant

import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse
import msyc.eati.domain.menu.service.RestaurantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantService,
) {

    /**
     * 레스토랑 목록 조회
     */
    @GetMapping
    fun getRestaurants(): ResponseEntity<List<RestaurantResponse>> {
        val restaurants = restaurantService.getAllRestaurants()
        return ResponseEntity.ok(restaurants)
    }

    /**
     * 레스토랑 단건 조회
     */
    @GetMapping("/{restaurantId}")
    fun getRestaurant(
        @PathVariable restaurantId: String
    ): ResponseEntity<RestaurantResponse> {
        val restaurant = restaurantService.getRestaurant(restaurantId)
        return ResponseEntity.ok(restaurant)
    }
}
