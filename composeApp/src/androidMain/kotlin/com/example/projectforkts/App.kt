package com.example.projectforkts

import android.app.Application
import com.example.projectforkts.di.dataStoreModule
import com.example.projectforkts.di.databaseModule
import com.example.projectforkts.di.networkModule
import com.example.projectforkts.di.repositoryModule
import com.example.projectforkts.di.storageModule
import com.example.projectforkts.di.useCaseModule
import com.example.projectforkts.di.viewModelModule
import com.example.projectforkts.main.data.db.appContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                dataStoreModule,
                databaseModule,
                storageModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}