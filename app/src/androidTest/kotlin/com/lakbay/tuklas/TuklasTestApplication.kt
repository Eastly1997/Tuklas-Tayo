package com.lakbay.tuklas

import android.app.Application
import com.lakbay.tuklas.auth.domain.AuthRepository
import com.lakbay.tuklas.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/** Test-only Application replaces DI before MainActivity resolves its ViewModel. */
class TuklasTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TestAuthRepository.reset()
        startKoin {
            androidContext(this@TuklasTestApplication)
            allowOverride(true)
            modules(appModules(false) + module { single<AuthRepository> { TestAuthRepository } })
        }
    }
}
