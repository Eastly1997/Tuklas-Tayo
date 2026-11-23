package com.lakbay.tuklas.auth.login

import com.lakbay.tuklas.app.R

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton

/** The same official SDK button previously used by Login; authentication stays caller-owned. */
@Composable
internal fun GoogleSignInButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dark = isSystemInDarkTheme()
    if (LocalInspectionMode.current) {
        Text(stringResource(R.string.login_google_button), modifier.heightIn(min = 48.dp))
    } else {
        AndroidView(factory = { SignInButton(it) }, modifier = modifier.fillMaxWidth().heightIn(min = 48.dp),
            update = { button ->
                button.setSize(SignInButton.SIZE_WIDE)
                button.setColorScheme(if (dark) SignInButton.COLOR_DARK else SignInButton.COLOR_LIGHT)
                button.isEnabled = enabled
                button.setOnClickListener { onClick() }
            })
    }
}
