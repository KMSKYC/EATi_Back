package msyc.eati.adapter.outbound.persistence.restaurant

import msyc.eati.domain.restaurant.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, String> {
    fun findAllByDeletedAtIsNull(): List<Category>
    fun findAllByParentIdAndDeletedAtIsNull(parentId: String): List<Category>
}
