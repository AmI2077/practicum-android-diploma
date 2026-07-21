package ru.practicum.android.diploma.feature.favourites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.models.details.VacancyDetails

interface FavouritesRepository {

//    fun getAllFavouritesVacancies(): Flow<VacancyDetails>
      fun getAllFavouritesVacancies(): Flow<List<VacancyDetails>> // Мы получаем список избранных вакансий, думаю, что должно быть List
}
