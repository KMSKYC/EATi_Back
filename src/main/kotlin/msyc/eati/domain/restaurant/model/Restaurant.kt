package msyc.eati.domain.restaurant.model

import jakarta.persistence.*
import msyc.eati.common.util.IdGenerator
import java.time.LocalDateTime

@Entity
@Table(name = "restaurants")
data class Restaurant(
    @Id
    @Column(name = "restaurant_id", length = 10, nullable = false, updatable = false)
    var restaurantId: String? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: String? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var address: String,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (restaurantId == null) restaurantId = IdGenerator.generate()
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
