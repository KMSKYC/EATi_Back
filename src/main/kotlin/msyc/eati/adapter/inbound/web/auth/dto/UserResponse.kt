package msyc.eati.adapter.inbound.web.auth.dto

import msyc.eati.domain.user.model.UserRole
import java.time.LocalDate

data class UserResponse(
    val userId: String,
    val email: String,
    val nickname: String,
    val birthdate: LocalDate?,
    val gender: String,
    val region: String?,
    val role: UserRole
)
