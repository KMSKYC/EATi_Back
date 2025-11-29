package msyc.eati.adapter.out.persistence.user

import msyc.eati.domain.user.model.UserTastes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTastesRepository : JpaRepository<UserTastes, String>
