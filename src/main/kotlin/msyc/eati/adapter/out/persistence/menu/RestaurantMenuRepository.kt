package msyc.eati.adapter.out.persistence.menu

import msyc.eati.domain.menu.model.RestaurantMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantMenuRepository : JpaRepository<RestaurantMenu, String>
