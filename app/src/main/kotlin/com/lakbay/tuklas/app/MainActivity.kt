package com.lakbay.tuklas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lakbay.tuklas.app.navigation.TuklasApp
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { TuklasTheme { TuklasApp() } }
    }
}

