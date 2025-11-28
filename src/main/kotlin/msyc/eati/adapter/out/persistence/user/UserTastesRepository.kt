package msyc.eati.adapter.out.persistence.user

import msyc.eati.domain.user.model.UserTastes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserTastesRepository : JpaRepository<UserTastes, String> {
    fun findByUserId(userId: String): Optional<UserTastes>

    @Modifying
    @Transactional
    fun deleteByUserId(userId: String)
}
