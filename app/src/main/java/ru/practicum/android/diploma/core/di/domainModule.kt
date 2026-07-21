package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase
import ru.practicum.android.diploma.feature.favourites.data.repository.FavouritesRepositoryImpl
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractor
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractorImpl
import ru.practicum.android.diploma.feature.search.domain.usecase.SearchVacanciesUseCase

val domainModule = module {

    single {
        GetVacancyDetailUseCase(get())
    }

    single {
        SearchVacanciesUseCase(get())
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(
            vacancyDao = get(),
            dispatcher = get()
        )
    }

    single<FavouritesInteractor> {
        FavouritesInteractorImpl(
            repository = get()
        )
    }

}
