package ru.practicum.android.diploma.feature.favourites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.models.details.VacancyDetails

interface FavouritesRepository {

    fun getAllFavouritesVacancies():
        Flow<List<VacancyDetails>>

    suspend fun addVacancyToFavourites(
        vacancy: VacancyDetails
    )

    suspend fun deleteVacancyFromFavourites(
        vacancy: VacancyDetails
    )

    suspend fun getFavouriteVacancyById(
        vacancyId: String
    ): VacancyDetails?

    suspend fun isFavourite(
        vacancyId: String
    ): Boolean

}
