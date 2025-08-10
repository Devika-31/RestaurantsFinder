package com.dev.restaurantsfinder.domain.model.restaurants

import com.google.gson.annotations.SerializedName

data class GetRestaurantRequestModel(
    val term: String = "restaurants",
    val location: String = "New York City",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radius: Int? = 500,
    val sort_by: String = "distance",
    val limit: Int? = 15,
    val offset: Int = 0
)

data class RestaurantDataModel(
    val id: String,
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    val rating: Double?,
    val distance: Double?,
    val coordinates: Coordinates?,
    val location: YelpLocation?,
    val phone: String?
) {
    data class Coordinates(val latitude: Double, val longitude: Double)
    data class YelpLocation(val address1: String?, val address2: String?, val city: String?)
}

data class GetRestaurantsResponseModel(
    val total: Int,
    val businesses: List<RestaurantDataModel>
)

