package msyc.eati.adapter.outbound.persistence.restaurant

import msyc.eati.domain.restaurant.model.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, String> {
    fun findAllByDeletedAtIsNull(): List<Restaurant>
    fun findByRestaurantIdAndDeletedAtIsNull(restaurantId: String): Restaurant?
}
