package msyc.eati.adapter.out.persistence.menu

import msyc.eati.domain.menu.model.RestaurantMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface RestaurantMenuRepository : JpaRepository<RestaurantMenu, String> {
    fun findByName(name: String): Optional<RestaurantMenu>
    fun existsByName(name: String): Boolean

    @Modifying
    @Transactional
    fun deleteByName(name: String)
}
