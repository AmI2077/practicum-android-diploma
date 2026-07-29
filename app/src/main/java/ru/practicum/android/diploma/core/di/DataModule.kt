package ru.practicum.android.diploma.core.di

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.core.database.AppDatabase
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.client.RetrofitClient
import ru.practicum.android.diploma.core.utils.HtmlParser
import ru.practicum.android.diploma.feature.detail.data.repository.DetailsRepositoryImpl
import ru.practicum.android.diploma.feature.detail.domain.api.DetailsRepository
import ru.practicum.android.diploma.feature.detail.utils.DetailsHtmlParserImpl
import ru.practicum.android.diploma.feature.favourites.data.repository.FavouritesRepositoryImpl
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository
import ru.practicum.android.diploma.feature.filter.data.IndustryRepositoryImpl
import ru.practicum.android.diploma.feature.filter.domain.api.IndustryRepository
import ru.practicum.android.diploma.feature.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository
import ru.practicum.android.diploma.feature.sharing.data.SharingRepositoryImpl
import ru.practicum.android.diploma.feature.sharing.domain.SharingInteractor
import ru.practicum.android.diploma.feature.sharing.domain.SharingInteractorImpl
import ru.practicum.android.diploma.feature.sharing.domain.SharingRepository

val dataModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get(),
            dispatcher = get()
        )
    }

    single<DetailsRepository> {
        DetailsRepositoryImpl(
            networkClient = get(),
            dispatcher = get(),
            htmlParser = get()
        )
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(
            vacancyDao = get(),
            dispatcher = get()
        )
    }

    single<VacancyDao> {
        get<AppDatabase>().getVacancyDao()
    }

    single<NetworkClient> {
        RetrofitClient
    }

    single<AppDatabase> {
        AppDatabase.createInstance(androidContext())
    }

    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
    single<HtmlParser> { DetailsHtmlParserImpl() }

    single<SharingRepository> {
        SharingRepositoryImpl(
            context = androidContext()
        )
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "filter_prefs",
            android.content.Context.MODE_PRIVATE
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            repository = get()
        )
    }

    single<IndustryRepository> {
        IndustryRepositoryImpl(
            networkClient = get()
        )
    }
}
