package ru.practicum.android.diploma.feature.favourites.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository

class FavouritesRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val dispatcher: CoroutineDispatcher,
): FavouritesRepository {
    override fun getAllFavouritesVacancies(): Flow<VacancyDetails> {
        return vacancyDao.getAllVacanciesFromFavourites()
            .map {
                it.toModel()
            }.flowOn(dispatcher)
    }
}
