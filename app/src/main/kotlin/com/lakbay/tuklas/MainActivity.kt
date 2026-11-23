package com.lakbay.tuklas

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.lakbay.tuklas.auth.login.LoginViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lakbay.tuklas.navigation.TuklasApp
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        splash.setKeepOnScreenCondition { authViewModel.state.value.restoringSession }
        enableEdgeToEdge()
        setContent { TuklasTheme { TuklasApp(authViewModel) } }
    }
}

