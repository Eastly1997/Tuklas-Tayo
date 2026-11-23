package com.lakbay.tuklas

import android.app.Application
import com.lakbay.tuklas.app.BuildConfig
import com.lakbay.tuklas.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TuklasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TuklasApplication)
            modules(appModules(BuildConfig.USE_FIREBASE))
        }
    }
}

