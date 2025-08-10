package com.dev.restaurantsfinder.domain.exception

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

/**
 * Trace exceptions(api call or parse data or connection errors) &
 * depending on what exception returns [ApiError]
 *
 * */
fun traceErrorException(throwable: Throwable?): ApiError {

    return when (throwable) {

        is SocketTimeoutException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.TIMEOUT)
        }

        is IOException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.NO_CONNECTION)
        }

        else -> ApiError(throwable?.message, 0, ApiError.ErrorStatus.UNKNOWN_ERROR)
    }
}