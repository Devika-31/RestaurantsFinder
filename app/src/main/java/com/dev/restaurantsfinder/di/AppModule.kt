package com.dev.restaurantsfinder.di

import com.dev.restaurantsfinder.domain.viewmodel.user.RestaurantsViewModel

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModels = module {
    //user
    viewModel { RestaurantsViewModel(get()) }

}
val repositories = module {
    //user
    single { createRestaurantsRepository(get(named("NormalService"))) }

}

val useCases = module {
    //user
    single { createRestaurantsUseCase(get()) }



}