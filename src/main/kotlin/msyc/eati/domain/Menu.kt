package msyc.eati.domain

import jakarta.persistence.*
import msyc.eati.common.IdGenerator
import java.time.LocalDateTime

@Entity
@Table(name = "menus")
data class Menu(
    @Id
    @Column(name = "menu_id", length = 10, nullable = false, updatable = false)
    var menuId: String? = null,

    @Column(name = "category_id", length = 10, nullable = false, updatable = false)
    var categoryId: String? = null,

    @Column
    var name: String? = null,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(name = "image_url", columnDefinition = "text")
    var imageUrl: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (menuId == null) menuId = IdGenerator.generate()
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
