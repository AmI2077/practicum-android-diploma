package ru.practicum.android.diploma.feature.favourites.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.core.models.details.toCard
import ru.practicum.android.diploma.feature.favourites.domain.api.FavouritesRepository

class FavouritesInteractorImpl(
    private val repository: FavouritesRepository
) : FavouritesInteractor {

    override fun getAllFavouritesVacancies():
        Flow<List<VacancyCard>> {

        return repository.getAllFavouritesVacancies()
            .map { vacancies ->
                vacancies.map {
                    it.toCard()
                }
            }
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
