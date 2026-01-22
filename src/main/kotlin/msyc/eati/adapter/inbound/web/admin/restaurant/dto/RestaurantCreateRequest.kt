package msyc.eati.adapter.inbound.web.admin.restaurant.dto

import jakarta.validation.constraints.NotBlank

data class RestaurantCreateRequest(
    @field:NotBlank(message = "카테고리 ID는 필수입니다")
    val categoryId: String,

    @field:NotBlank(message = "레스토랑명은 필수입니다")
    val restaurantName: String,

    @field:NotBlank(message = "주소는 필수입니다")
    val address: String,

    val description: String? = null
)
