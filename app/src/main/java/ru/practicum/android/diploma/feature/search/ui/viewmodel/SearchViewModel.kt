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
import java.util.ArrayList


class SearchViewModel(
    private val searchVacanciesUseCase: SearchVacanciesUseCase
) : ViewModel() {


    private val _state =
        MutableLiveData<SearchState>(SearchState.Initial)
    val state: LiveData<SearchState> = _state

    private var searchJob: Job? = null
    private var currentQuery = ""
    private var currentPage = 0
    private var totalPages = 0
    private val vacancies = mutableListOf<VacancyCard>()
    private var isLoadingNextPage = false

    companion object {
        private const val SEARCH_DELAY = 2000L
    }

    fun search(query: String) {
        searchJob?.cancel()
        currentQuery = query

        if (query.isBlank()) {
            vacancies.clear()
            _state.value = SearchState.Initial
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            currentPage = 0
            totalPages = 0
            vacancies.clear()
            _state.value = SearchState.Loading

            loadVacancies()
        }
    }

    private suspend fun loadVacancies() {
        val params = VacancySearchParams(
            text = currentQuery,
            page = currentPage
        )

        when (val result = searchVacanciesUseCase(params)) {

            is Result.Content -> {

                totalPages = result.data.pages

                val newVacancies = result.data.vacancies.filter { vacancy ->
                    vacancies.none { it.id == vacancy.id }
                }

                vacancies.addAll(newVacancies)

                if (vacancies.isEmpty()) {
                    _state.value = SearchState.EmptyResult
                } else {
                    _state.value = SearchState.Content(
                        vacancies = vacancies.toList(),
                        isLoadingNextPage = isLoadingNextPage
                    )
                }
            }

            is Result.Error -> {
                _state.value = SearchState.Error(result.error)
            }
        }
    }
    fun loadNextPage() {

        if (isLoadingNextPage) return

        if (currentPage >= totalPages - 1) return

        searchJob = viewModelScope.launch {

            isLoadingNextPage = true

            _state.value = SearchState.Content(
                vacancies = vacancies.toList(),
                isLoadingNextPage = true
            )

            currentPage++

            loadVacancies()

            isLoadingNextPage = false

            _state.value = SearchState.Content(
                vacancies = vacancies.toList(),
                isLoadingNextPage = false
            )
        }
    }

    fun isLoadingNextPage(): Boolean {
        return isLoadingNextPage
    }
}

