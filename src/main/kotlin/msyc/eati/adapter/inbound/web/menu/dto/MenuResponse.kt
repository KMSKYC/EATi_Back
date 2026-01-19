package msyc.eati.adapter.inbound.web.menu.dto

data class MenuResponse(
    val menuId: String,
    val categoryId: String,
    val menuName: String?,
    val description: String?,
    val imageUrl: String?
)
