package msyc.eati.adapter.inbound.web.admin.category.dto

import jakarta.validation.constraints.NotBlank

data class CategoryUpdateRequest(
    val parentId: String? = null,

    @field:NotBlank(message = "카테고리명은 필수입니다")
    val categoryName: String
)
