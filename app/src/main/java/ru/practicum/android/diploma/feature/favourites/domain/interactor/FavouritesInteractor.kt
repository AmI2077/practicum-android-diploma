package ru.practicum.android.diploma.feature.favourites.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.details.VacancyDetails

interface FavouritesInteractor {

    fun getAllFavouritesVacancies():
        Flow<List<VacancyCard>>

    suspend fun addVacancyToFavourites(
        vacancy: VacancyDetails
    )

    suspend fun deleteVacancyFromFavourites(
        vacancy: VacancyDetails
    )

    suspend fun isVacancyFavourite(
        vacancyId: String
    ): Boolean

}
