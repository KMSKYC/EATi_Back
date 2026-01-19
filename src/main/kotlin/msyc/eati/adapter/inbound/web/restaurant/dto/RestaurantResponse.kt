package msyc.eati.adapter.inbound.web.restaurant.dto

data class RestaurantResponse(
    val categoryId: String,
    val restaurantId: String,
    val restaurantName: String,
    val address: String,
    val description: String?,
)
