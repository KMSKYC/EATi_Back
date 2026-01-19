package msyc.eati.adapter.outbound.persistence.menu

import msyc.eati.domain.menu.model.RestaurantMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantMenuRepository : JpaRepository<RestaurantMenu, String> {
    fun findByMenuId(menuId: String): List<RestaurantMenu>
}
