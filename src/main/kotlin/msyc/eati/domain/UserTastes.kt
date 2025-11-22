package msyc.eati.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_tastes")
data class UserTastes(
    @Id
    @Column(name = "user_id", length = 10, nullable = false, updatable = false)
    var userId: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(name = "like_category")
    var preferredCategories: String? = null,

    @Column(name = "dislike_category")
    var dislikedCategories: String? = null,

    @Column(name = "recent_menus")
    var recentMenus: String? = null,

    @Column(name = "restrictions")
    var menuRestrictions: String? = null,

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
