package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.detail.presentation.viewmodel.VacancyDetailViewModel

val viewModelModule = module {
    viewModel { VacancyDetailViewModel(get()) }
}
