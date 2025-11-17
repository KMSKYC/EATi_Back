package msyc.eati.repository

import msyc.eati.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean

    @Modifying
    @Transactional
    fun deleteByEmail(email: String)
}
