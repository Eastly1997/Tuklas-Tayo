package com.lakbay.tuklas.di

import com.lakbay.tuklas.auth.di.authModule
import com.lakbay.tuklas.feature.discover.di.discoverModules

fun appModules(useFirebase: Boolean) = discoverModules(useFirebase) + authModule
