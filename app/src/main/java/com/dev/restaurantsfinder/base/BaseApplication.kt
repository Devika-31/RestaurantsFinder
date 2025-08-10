package com.dev.restaurantsfinder.base

import android.app.Application
import android.content.Context
import com.dev.restaurantsfinder.di.NetworkModule
import com.dev.restaurantsfinder.di.repositories
import com.dev.restaurantsfinder.di.useCases
import com.dev.restaurantsfinder.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : Application() {
    companion object {
        @Volatile
        private lateinit var instance: BaseApplication
        val mContext: Context
            get() = instance.applicationContext

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //koin initialization
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(viewModels, repositories, useCases, NetworkModule))
        }

    }


}