package com.dev.restaurantsfinder.domain.usecase

import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantRequestModel
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantsResponseModel
import com.dev.restaurantsfinder.domain.repository.RestaurantRepository
import com.dev.restaurantsfinder.domain.usecase.base.UseCase


class GetRestaurantsUseCase(private val userRepository: RestaurantRepository) :
    UseCase<GetRestaurantsResponseModel, Any?>() {

    override suspend fun run(params: Any?): GetRestaurantsResponseModel {
        return userRepository.getRestaurants(params as GetRestaurantRequestModel)
    }
}
