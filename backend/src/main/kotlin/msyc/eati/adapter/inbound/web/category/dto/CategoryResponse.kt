package msyc.eati.adapter.inbound.web.category.dto

data class CategoryResponse(
    val categoryId: String,
    val parentId: String?,
    val categoryName: String,
    val children: List<CategoryResponse> = emptyList()
)
