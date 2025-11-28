package msyc.eati.domain.user.model

import jakarta.persistence.*
import msyc.eati.common.util.IdGenerator
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "user_id", length = 10, nullable = false, updatable = false)
    var userId: String? = null,

    @Column(nullable = false, length = 255)
    var email: String,

    @Column(nullable = false, length = 255)
    var password: String,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(nullable = false, length = 50)
    var nickname: String,

    @Column
    var birthdate: LocalDate? = null,

    @Column(nullable = false, length = 10)
    var gender: String,

    @Column
    var region: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @Column(name = "last_login_at")
    var lastLoginAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (userId == null) userId = IdGenerator.generate()
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
