package com.example.projectforkts

import android.app.Application
import com.example.projectforkts.di.authModule
import com.example.projectforkts.di.dataStoreModule
import com.example.projectforkts.di.databaseModule
import com.example.projectforkts.di.networkModule
import com.example.projectforkts.di.repositoryModule
import com.example.projectforkts.di.storageModule
import com.example.projectforkts.di.useCaseModule
import com.example.projectforkts.di.viewModelModule
import com.example.projectforkts.main.data.db.appContext
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        Napier.base(CrashlyticsAntilog())
        appContext = this
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                authModule,
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