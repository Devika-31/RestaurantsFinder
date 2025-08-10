package com.dev.restaurantsfinder.data.repository_impl

import com.dev.restaurantsfinder.data.source.remote.ApiService
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantRequestModel
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantsResponseModel
import com.dev.restaurantsfinder.domain.repository.RestaurantRepository

class RestaurantsRepositoryImpl(private val apiService: ApiService) : RestaurantRepository {


    override suspend fun getRestaurants(request: GetRestaurantRequestModel): GetRestaurantsResponseModel {

        return apiService.getRestaurantsApi(
            latitude = request.latitude,
            longitude = request.longitude,
            radius = request.radius,
            offset = request.offset,
            term = request.term,
            location = request.location,
            sortBy = request.sort_by,
            limit = request.limit
        )
    }

}