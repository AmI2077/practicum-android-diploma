package ru.practicum.android.diploma.feature.favourites.ui.viewmodel

import ru.practicum.android.diploma.core.models.card.VacancyCard

sealed interface FavouritesState {

    data object Loading : FavouritesState

    data class Content(
        val vacancies: List<VacancyCard>
    ) : FavouritesState

    data object Empty : FavouritesState

    data object Error : FavouritesState
}
