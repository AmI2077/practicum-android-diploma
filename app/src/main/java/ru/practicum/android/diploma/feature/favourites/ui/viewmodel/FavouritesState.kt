package ru.practicum.android.diploma.feature.favourites.ui.viewmodel

import ru.practicum.android.diploma.core.models.details.VacancyDetails

sealed interface FavouritesState {

    data object Loading : FavouritesState

    data class Content(
        val vacancies: List<VacancyDetails>
    ) : FavouritesState

    data object Empty : FavouritesState

    data object Error : FavouritesState
}
