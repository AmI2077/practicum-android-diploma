package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase
import ru.practicum.android.diploma.feature.favourites.data.repository.FavouritesRepositoryImpl
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractor
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractorImpl
import ru.practicum.android.diploma.feature.filter.domain.interactor.FilterInteractor
import ru.practicum.android.diploma.feature.filter.domain.interactor.FilterInteractorImpl
import ru.practicum.android.diploma.feature.filter.domain.interactor.GetIndustriesInteractor
import ru.practicum.android.diploma.feature.filter.domain.interactor.GetIndustriesInteractorImpl
import ru.practicum.android.diploma.feature.search.domain.usecase.SearchVacanciesUseCase

val domainModule = module {

    factory {
        GetVacancyDetailUseCase(get())
    }

    factory {
        SearchVacanciesUseCase(get())
    }

    factory<FavouritesInteractor> {
        FavouritesInteractorImpl(
            repository = get()
        )
    }

    factory<GetIndustriesInteractor> {
        GetIndustriesInteractorImpl(
            repository = get()
        )
    }
    factory<FilterInteractor> {
        FilterInteractorImpl(get())
    }


}
