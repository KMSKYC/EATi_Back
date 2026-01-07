package msyc.eati.domain.user.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
@Table(name = "users_tastes", schema = "eati")
data class UserTastes(
    @Id
    @Column(name = "user_id", length = 10, nullable = false, updatable = false)
    var userId: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "liked_category_ids", nullable = false, columnDefinition = "jsonb")
    var likedCategoryIds: List<String>,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "disliked_category_ids", nullable = false, columnDefinition = "jsonb")
    var dislikedCategoryIds: List<String>,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recent_menus")
    var recentMenus: List<String>? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "restrict_menus")
    var menuRestrictions: List<String>? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
