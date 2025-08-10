package com.dev.restaurantsfinder.domain.usecase.base

import com.dev.restaurantsfinder.domain.exception.ApiError


interface UseCaseResponse<Type> {
    fun onSuccess(result: Type)
    fun onError(apiError: ApiError?)
}