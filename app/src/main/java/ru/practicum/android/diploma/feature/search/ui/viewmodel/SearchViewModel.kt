package ru.practicum.android.diploma.feature.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.feature.search.domain.usecase.SearchVacanciesUseCase


class SearchViewModel(
    private val searchVacanciesUseCase: SearchVacanciesUseCase
) : ViewModel() {


    private val _state =
        MutableLiveData<SearchState>(SearchState.Initial)

    val state: LiveData<SearchState> = _state


    private var searchJob: Job? = null


    companion object {
        private const val SEARCH_DELAY = 2000L
    }


    fun search(query: String) {

        searchJob?.cancel()


        if (query.isBlank()) {
            _state.value = SearchState.Initial
            return
        }


        searchJob = viewModelScope.launch {

            delay(SEARCH_DELAY)


            _state.value = SearchState.Loading


            val params = VacancySearchParams(
                text = query
            )


            when(
                val result =
                    searchVacanciesUseCase(params)
            ) {

                is Result.Content -> {

                    if (result.data.isEmpty()) {

                        _state.value =
                            SearchState.EmptyResult

                    } else {

                        _state.value =
                            SearchState.Content(
                                result.data
                            )
                    }
                }
                is Result.Error -> {

                    _state.value =
                        SearchState.Error(
                            result.error
                        )
                }
            }
        }
    }
}

sealed interface SearchState {
    data object Initial : SearchState
    data object Loading : SearchState
    data class Content(
        val vacancies: List<VacancyCard>
    ) : SearchState
    data object EmptyResult : SearchState
    data class Error(
        val error: NetworkErrors
    ) : SearchState
}
