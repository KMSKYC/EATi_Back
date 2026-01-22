package msyc.eati.adapter.inbound.web.admin.restaurant

import jakarta.validation.Valid
import msyc.eati.adapter.inbound.web.admin.restaurant.dto.RestaurantCreateRequest
import msyc.eati.adapter.inbound.web.admin.restaurant.dto.RestaurantUpdateRequest
import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse
import msyc.eati.domain.menu.service.RestaurantService
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
@RequestMapping("/api/admin/restaurants")
class AdminRestaurantController(
    private val restaurantService: RestaurantService
) {

    /**
     * 레스토랑 등록
     */
    @PostMapping
    fun createRestaurant(
        @Valid @RequestBody request: RestaurantCreateRequest
    ): ResponseEntity<RestaurantResponse> {
        val restaurant = restaurantService.createRestaurant(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant)
    }

    /**
     * 레스토랑 수정
     */
    @PutMapping("/{restaurantId}")
    fun updateRestaurant(
        @PathVariable restaurantId: String,
        @Valid @RequestBody request: RestaurantUpdateRequest
    ): ResponseEntity<RestaurantResponse> {
        val restaurant = restaurantService.updateRestaurant(restaurantId, request)
        return ResponseEntity.ok(restaurant)
    }

    /**
     * 레스토랑 삭제
     */
    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurant(
        @PathVariable restaurantId: String
    ): ResponseEntity<Void> {
        restaurantService.deleteRestaurant(restaurantId)
        return ResponseEntity.noContent().build()
    }
}
