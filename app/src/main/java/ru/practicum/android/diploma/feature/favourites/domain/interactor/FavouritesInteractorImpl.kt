package ru.practicum.android.diploma.feature.favourites.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository


class FavouritesInteractorImpl(
    private val repository: FavouritesRepository
) : FavouritesInteractor {

    override fun getAllFavouritesVacancies():
        Flow<List<VacancyDetails>> {

        return repository.getAllFavouritesVacancies()
    }

    override suspend fun addVacancyToFavourites(
        vacancy: VacancyDetails
    ) {

        repository.addVacancyToFavourites(vacancy)
    }

    override suspend fun deleteVacancyFromFavourites(
        vacancy: VacancyDetails
    ) {

        repository.deleteVacancyFromFavourites(vacancy)
    }

    override suspend fun isVacancyFavourite(
        vacancyId: String
    ): Boolean {

        return repository
            .getFavouriteVacancyById(vacancyId) != null
    }
}
