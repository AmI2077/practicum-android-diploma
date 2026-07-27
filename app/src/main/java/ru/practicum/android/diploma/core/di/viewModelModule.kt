package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.feature.favourites.ui.viewmodel.FavouritesViewModel
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterViewModel
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryViewModel
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchViewModel

val viewModelModule = module {

    viewModel {
        VacancyDetailViewModel(
            getVacancyDetailUseCase = get(),
            favouritesInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        IndustryViewModel(
            getIndustriesInteractor = get()
        )
    }

    viewModel {
        FilterViewModel()
    }
}
