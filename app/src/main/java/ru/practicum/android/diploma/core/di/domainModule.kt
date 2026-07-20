package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase

val domainModule = module {
    single { GetVacancyDetailUseCase(get()) }
}
