package msyc.eati.repository

import msyc.eati.domain.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, String> {
    fun findByName(name: String): Optional<Restaurant>
    fun existsByName(name: String): Boolean

    @Modifying
    @Transactional
    fun deleteByName(name: String)
}
