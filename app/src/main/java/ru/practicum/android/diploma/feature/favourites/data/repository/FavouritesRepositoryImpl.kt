package ru.practicum.android.diploma.feature.favourites.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.extensions.toEntity
import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository

class FavouritesRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val dispatcher: CoroutineDispatcher,
) : FavouritesRepository {

    override fun getAllFavouritesVacancies(): Flow<List<VacancyDetails>> {
        return vacancyDao
            .getAllVacanciesFromFavourites()
            .map { entities ->
                entities.map { it.toModel() }
            }
    }

    override suspend fun addVacancyToFavourites(vacancy: VacancyDetails) {
        withContext(dispatcher) {
            vacancyDao.insertVacancyToFavourites(vacancy.toEntity())
        }
    }

    override suspend fun deleteVacancyFromFavourites(vacancy: VacancyDetails) {
        withContext(dispatcher) {
            vacancyDao.deleteVacancyFromFavourites(vacancy.toEntity())
        }
    }

    override suspend fun getFavouriteVacancyById(
        vacancyId: String
    ): VacancyDetails? {
        return withContext(dispatcher) {
            vacancyDao
                .getVacancyFromFavouritesById(vacancyId)
                ?.toModel()
        }
    }
}
