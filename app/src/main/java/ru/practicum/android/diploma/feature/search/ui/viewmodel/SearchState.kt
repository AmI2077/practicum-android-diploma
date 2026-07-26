package ru.practicum.android.diploma.feature.search.ui.viewmodel

import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.card.VacancyCard

sealed interface SearchState {
    data object Initial : SearchState
    data object Loading : SearchState
    data class Content(
        val vacancies: List<VacancyCard>,
        val totalFound: Int = 0,
        val isLoadingNextPage: Boolean = false,
        val isNewSearch: Boolean = false
    ) : SearchState
    data object EmptyResult : SearchState
    data class Error(
        val error: NetworkErrors
    ) : SearchState
}
