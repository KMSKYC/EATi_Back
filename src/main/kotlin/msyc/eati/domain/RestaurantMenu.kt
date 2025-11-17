package msyc.eati.domain

import jakarta.persistence.*
import msyc.eati.common.IdGenerator
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

data class RestaurantMenuId(
    var menuId: String? = null,
    var restaurantId: String? = null,
    var categoryId: String? = null
) : Serializable

@Entity
@Table(name = "restaurant_menus")
@IdClass(RestaurantMenuId::class)
data class RestaurantMenu(
    @Id
    @Column(name = "menu_id", length = 10, nullable = false, updatable = false)
    var menuId: String? = null,

    @Id
    @Column(name = "restaurant_id", length = 10, nullable = false, updatable = false)
    var restaurantId: String? = null,

    @Id
    @Column(name = "category_id", length = 10, nullable = false, updatable = false)
    var categoryId: String? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,

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
