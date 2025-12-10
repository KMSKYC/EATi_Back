package msyc.eati.adapter.outbound.persistence.menu

import msyc.eati.domain.menu.model.Menu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : JpaRepository<Menu, String> {
    fun findAllByDeletedAtIsNull(): List<Menu>
    fun findAllByCategoryIdAndDeletedAtIsNull(categoryId: String): List<Menu>
    fun findByMenuIdAndDeletedAtIsNull(menuId: String): Menu?
}
