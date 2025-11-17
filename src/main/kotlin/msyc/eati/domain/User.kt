package msyc.eati.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "user_id", columnDefinition = "uuid", nullable = false, updatable = false)
    var userId: UUID? = null,

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

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    var createdAt: OffsetDateTime,

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    var updatedAt: OffsetDateTime,

    @Column(name = "deleted_at", columnDefinition = "timestamptz")
    var deletedAt: OffsetDateTime? = null,

    @Column(name = "last_login_at", columnDefinition = "timestamptz")
    var lastLoginAt: OffsetDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (userId == null) userId = UUID.randomUUID()
        val now = OffsetDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = OffsetDateTime.now()
    }
}