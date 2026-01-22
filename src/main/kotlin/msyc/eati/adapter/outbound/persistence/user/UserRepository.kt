package msyc.eati.adapter.outbound.persistence.user

import msyc.eati.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {
	fun findByEmail(email: String): Optional<User>
	fun existsByEmail(email: String): Boolean
	fun findAllByDeletedAtIsNull(): List<User>
	fun findByUserIdAndDeletedAtIsNull(userId: String): User?
}
