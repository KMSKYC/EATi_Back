package msyc.eati.adapter.inbound.web.admin.restaurantmenu.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class RestaurantMenuCreateRequest(
    @field:NotBlank(message = "메뉴 ID는 필수입니다")
    val menuId: String,

    @field:NotBlank(message = "레스토랑 ID는 필수입니다")
    val restaurantId: String,

    @field:NotBlank(message = "카테고리 ID는 필수입니다")
    val categoryId: String,

    @field:NotBlank(message = "레스토랑 메뉴명은 필수입니다")
    val restaurantMenuName: String,

    @field:NotNull(message = "가격은 필수입니다")
    @field:Positive(message = "가격은 0보다 커야 합니다")
    val price: BigDecimal,

    val description: String? = null
)
