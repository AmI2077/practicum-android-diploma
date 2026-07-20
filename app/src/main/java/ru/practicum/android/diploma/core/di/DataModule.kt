package ru.practicum.android.diploma.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.core.database.AppDatabase
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.client.RetrofitClient
import ru.practicum.android.diploma.feature.detail.data.repository.DetailsRepositoryImpl
import ru.practicum.android.diploma.feature.detail.domain.api.DetailsRepository
import ru.practicum.android.diploma.feature.favourites.data.repository.FavouritesRepositoryImpl
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository
import ru.practicum.android.diploma.feature.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository

val dataModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<DetailsRepository> {
        DetailsRepositoryImpl(get(), get(), get())
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<VacancyDao> {
        get<AppDatabase>().getVacancyDao()
    }

    single<NetworkClient> { RetrofitClient }
    single<AppDatabase> { AppDatabase.createInstance(androidContext()) }
    single<CoroutineDispatcher> { Dispatchers.IO }
}
