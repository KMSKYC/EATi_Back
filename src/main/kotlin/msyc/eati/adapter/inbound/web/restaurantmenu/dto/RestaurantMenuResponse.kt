package msyc.eati.adapter.inbound.web.restaurantmenu.dto

import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.adapter.inbound.web.restaurant.dto.RestaurantResponse

data class RestaurantMenuResponse(
    val restaurantMenuId: String,
    val categoryId: String,
    val restaurantId: String,
    val menuId: String,
    val restaurantMenuName: String,
    val price: String,
    val description: String?,
    val menu: MenuResponse?,
    val restaurant: RestaurantResponse?
)
