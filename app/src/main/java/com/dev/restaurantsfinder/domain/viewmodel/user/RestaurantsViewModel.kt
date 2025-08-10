package com.dev.restaurantsfinder.domain.viewmodel.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.restaurantsfinder.domain.exception.ApiError
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantRequestModel
import com.dev.restaurantsfinder.domain.model.restaurants.GetRestaurantsResponseModel
import com.dev.restaurantsfinder.domain.usecase.GetRestaurantsUseCase
import com.dev.restaurantsfinder.domain.usecase.base.UseCaseResponse


class RestaurantsViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
) : ViewModel() {

    val getRestaurantsResponseData = MutableLiveData<GetRestaurantsResponseModel>()
    val progressBar: MutableLiveData<Boolean> = MutableLiveData()
    val messageData: MutableLiveData<String> = MutableLiveData()

    fun callFetchRestApi(request: GetRestaurantRequestModel) {
        progressBar.value = true
        getRestaurantsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetRestaurantsResponseModel> {
                override fun onSuccess(result: GetRestaurantsResponseModel) {
                    getRestaurantsResponseData.value = result
                    progressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    progressBar.value = false
                }

            })
    }


}