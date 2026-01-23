package msyc.eati.adapter.inbound.web.admin.menu.dto

import jakarta.validation.constraints.NotBlank

data class MenuCreateRequest(
    @field:NotBlank(message = "카테고리 ID는 필수입니다")
    val categoryId: String,

    @field:NotBlank(message = "메뉴명은 필수입니다")
    val menuName: String,

    val description: String? = null,

    val imageUrl: String? = null
)
