package com.lakbay.tuklas.feature.manage.dashboard

import com.lakbay.tuklas.feature.manage.R

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun DashboardScreen() {
    Column(Modifier.fillMaxSize().padding(TuklasSpacing.large)) {
        Text(stringResource(R.string.dashboard_management_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.dashboard_placeholder),
            style = MaterialTheme.typography.bodyLarge)
    }
}