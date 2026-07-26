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

    private val _state = MutableLiveData<SearchState>(SearchState.Initial)
    val state: LiveData<SearchState> = _state

    private var searchJob: Job? = null
    private var currentQuery = ""
    private var currentPage = 1
    private var totalPages = 0
    private val vacancies = mutableListOf<VacancyCard>()
    private var isLoadingNextPage = false
    private var isNewSearch = false

    private var lastSearchResults: SearchState.Content? = null

    companion object {
        private const val SEARCH_DELAY = 2000L
    }

    init {
        restoreStateIfNeeded()
    }

    fun search(query: String) {
        searchJob?.cancel()
        if (currentQuery == query && lastSearchResults != null) {
            return
        }
        currentQuery = query

        if (query.isBlank()) {
            vacancies.clear()
            currentPage = 1
            totalPages = 0
            isLoadingNextPage = false
            isNewSearch = false
            lastSearchResults = null
            _state.value = SearchState.Initial
            return
        }

        isNewSearch = true

        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            currentPage = 1
            totalPages = 0
            vacancies.clear()
            isLoadingNextPage = false
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
                    lastSearchResults = null
                    _state.value = SearchState.EmptyResult
                } else {
                    val contentState = SearchState.Content(
                        vacancies = vacancies.toList(),
                        isLoadingNextPage = isLoadingNextPage,
                        isNewSearch = isNewSearch
                    )
                    lastSearchResults = contentState
                    _state.value = contentState
                    isNewSearch = false
                }
            }
            is Result.Error -> {
                lastSearchResults = null
                _state.value = SearchState.Error(result.error)
                isNewSearch = false
            }
        }
    }

    fun loadNextPage() {
        val nextPage = currentPage + 1

        if (isLoadingNextPage || nextPage >= totalPages || currentQuery.isBlank()) {
            return
        }

        isNewSearch = false

        searchJob = viewModelScope.launch {
            isLoadingNextPage = true

            _state.value = SearchState.Content(
                vacancies = vacancies.toList(),
                isLoadingNextPage = true,
                isNewSearch = false
            )

            currentPage++
            loadVacancies()

            isLoadingNextPage = false

            val contentState = SearchState.Content(
                vacancies = vacancies.toList(),
                isLoadingNextPage = false,
                isNewSearch = false
            )
            lastSearchResults = contentState
            _state.value = contentState
        }
    }

    fun restoreStateIfNeeded() {
        if (currentQuery.isNotEmpty() && lastSearchResults != null) {
            _state.value = lastSearchResults!!
        } else if (currentQuery.isNotEmpty() && _state.value !is SearchState.Content) {
            search(currentQuery)
        }
    }

    fun isLoadingNextPage(): Boolean {
        return isLoadingNextPage
    }
}
