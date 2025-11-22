package msyc.eati.repository

import msyc.eati.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, String> {
    fun findByName(name: String): Optional<Category>
    fun existsByName(name: String): Boolean

    @Modifying
    @Transactional
    fun deleteByName(name: String)
}
