package com.lakbay.tuklas.feature.discover.di

import com.lakbay.tuklas.feature.discover.presentation.DiscoverViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val discoverModule = module {
    viewModelOf(::DiscoverViewModel)
}
