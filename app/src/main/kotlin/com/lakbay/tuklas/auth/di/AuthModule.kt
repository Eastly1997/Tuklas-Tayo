package com.lakbay.tuklas.auth.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.lakbay.tuklas.app.BuildConfig
import com.lakbay.tuklas.auth.data.FirebaseAuthRepository
import com.lakbay.tuklas.auth.domain.*
import com.lakbay.tuklas.auth.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        FirebaseAuthRepository(if (BuildConfig.AUTH_CONFIGURED) FirebaseAuth.getInstance() else null,
            CredentialManager.create(androidContext()))
    }
    factory { ObserveAuthStateUseCase(get()) }
    factory { SignInWithGoogleUseCase(get()) }
    factory { SignOutUseCase(get()) }
    viewModel { LoginViewModel(get<AuthRepository>().configured, get(), get(), get()) }
}
