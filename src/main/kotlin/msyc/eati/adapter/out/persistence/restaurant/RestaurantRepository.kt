package msyc.eati.adapter.out.persistence.restaurant

import msyc.eati.domain.restaurant.model.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, String> {
}
