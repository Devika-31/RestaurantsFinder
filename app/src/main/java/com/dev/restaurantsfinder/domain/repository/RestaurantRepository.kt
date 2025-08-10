package com.dev.restaurantsfinder.domain.repository

import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantRequestModel
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantsResponseModel


interface RestaurantRepository {
    suspend fun getRestaurants(request: GetRestaurantRequestModel): GetRestaurantsResponseModel
}