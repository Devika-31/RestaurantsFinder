package com.dev.restaurantsfinder.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.dev.restaurantsfinder.data.repository_impl.RestaurantsRepositoryImpl
import com.dev.restaurantsfinder.data.source.remote.ApiNames.Companion.BASE_URL
import com.dev.restaurantsfinder.data.source.remote.ApiService
import com.dev.restaurantsfinder.domain.repository.RestaurantRepository
import com.dev.restaurantsfinder.domain.usecase.GetRestaurantsUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val TIME_OUT = 120L
val NetworkModule = module {

    single {
        GsonBuilder().create()
    }

    single {
        val context: Context = get()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer  ${BuildConfig.API_KEY}"
                )
                .build()
            chain.proceed(request)
        }

        //sync or to easy put api key here directly after Bearer for test


        OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Custom token interceptor
            .addInterceptor(logging)
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250_000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
    single<RestaurantRepository> { RestaurantsRepositoryImpl(get()) } // no named()

}


//login
fun createRestaurantsRepository(apiService: ApiService): RestaurantRepository {
    return RestaurantsRepositoryImpl(apiService)
}

fun createRestaurantsUseCase(userRepository: RestaurantRepository): GetRestaurantsUseCase {
    return GetRestaurantsUseCase(userRepository)
}


