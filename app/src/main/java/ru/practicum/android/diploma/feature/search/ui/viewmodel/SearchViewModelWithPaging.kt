package ru.practicum.android.diploma.feature.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterState
import ru.practicum.android.diploma.feature.search.data.SearchPagingSource
import ru.practicum.android.diploma.feature.search.domain.usecase.SearchVacanciesUseCase
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModelWithPaging(
    private val searchVacanciesUseCase: SearchVacanciesUseCase
): ViewModel() {

    private var currentFilters = FilterSettings()
    private val _filterState = MutableLiveData(FilterState())
    val filterState: LiveData<FilterState> = _filterState

    private var _totalFound = MutableLiveData<Int>()
    val totalFound = _totalFound

    val queryFlow = MutableStateFlow("")
    private val appliedFiltersFlow = MutableStateFlow(FilterSettings())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagingData: LiveData<PagingData<VacancyCard>> = combine(
        queryFlow.debounce(SEARCH_DELAY_MS.milliseconds),
        appliedFiltersFlow
    ) { query, filters ->
        Pair(query, filters)
    }
        .distinctUntilChanged()
        .flatMapLatest { (query, filters) ->
            if (query.isEmpty()) {
                flowOf(PagingData.empty())
            } else {
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        SearchPagingSource(
                            searchVacanciesUseCase = searchVacanciesUseCase,
                            query = query,
                            salary = filters.salary,
                            onlyWithSalary = filters.hideWithoutSalary,
                            industryId = filters.industry?.id
                        ) {
                            _totalFound.value = it
                        }
                    }
                ).flow
            }
        }.cachedIn(viewModelScope).asLiveData()


    fun search(query: String) {
        if (query.isBlank()) {
            queryFlow.value = ""
            return
        }
        queryFlow.value = query
    }

    fun applyFilters() {
        appliedFiltersFlow.value = currentFilters
    }

    private fun syncFilterState() {
        _filterState.value = FilterState(
            salary = currentFilters.salary,
            hideWithoutSalary = currentFilters.hideWithoutSalary,
            industry = currentFilters.industry
        )
    }

    fun saveSalary(text: String?) {
        currentFilters = currentFilters.copy(salary = text?.toInt() ?: 0)
        syncFilterState()
    }

    fun saveIndustry(filterIndustry: FilterIndustry?) {
        currentFilters = currentFilters.copy(industry = filterIndustry)
        syncFilterState()
    }

    fun saveHideWithoutSalary(checked: Boolean) {
        currentFilters = currentFilters.copy(hideWithoutSalary = checked)
        syncFilterState()
    }

    fun clearFilter() {
        currentFilters = FilterSettings()
        syncFilterState()
    }

    companion object {
        private const val SEARCH_DELAY_MS = 2000L
        private const val PAGE_SIZE = 20
    }
}
