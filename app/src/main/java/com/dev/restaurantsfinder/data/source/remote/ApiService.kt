package com.dev.restaurantsfinder.data.source.remote

import com.dev.restaurantsfinder.data.source.remote.ApiNames.Companion.GET_RESTAURANTS_API
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantsResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(GET_RESTAURANTS_API)
    suspend fun getRestaurantsApi(
        @Query("term") term: String,
        @Query("location") location: String?,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("radius") radius: Int?,
        @Query("sort_by") sortBy: String?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): GetRestaurantsResponseModel
}