package ru.practicum.android.diploma.core.di

import org.koin.dsl.module

val appModule = module {
    includes(
        dataModule,
        domainModule,
        viewModelModule
    )
}
