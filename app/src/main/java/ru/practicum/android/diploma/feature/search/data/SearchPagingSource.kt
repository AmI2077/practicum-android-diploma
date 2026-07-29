package ru.practicum.android.diploma.feature.search.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.feature.search.domain.usecase.SearchVacanciesUseCase

class SearchPagingSource(
    private val searchVacanciesUseCase: SearchVacanciesUseCase,
    private val query: String,
    private val salary: Int?,
    private val onlyWithSalary: Boolean,
    private val industryId: Int?,
    private val onTotalFoundLoaded: (totalPages: Int) -> Unit
) : PagingSource<Int, VacancyCard>() {

    private val loadedVacancyIds = mutableSetOf<String>()

    init {
        registerInvalidatedCallback {
            loadedVacancyIds.clear()
        }
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, VacancyCard> {

        val page = params.key ?: 1

        val searchParams = VacancySearchParams(
            text = query,
            page = page,
            salary = salary,
            onlyWithSalary = onlyWithSalary,
            industry = industryId
        )

        return when (
            val result = searchVacanciesUseCase(searchParams)
        ) {
            is Result.Content -> {
                val vacancies = result.data.vacancies.filter { vacancy ->
                    loadedVacancyIds.add(vacancy.id)
                }
                val totalPages = result.data.pages

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (page >= totalPages || vacancies.isEmpty()) null else page + 1

                onTotalFoundLoaded(result.data.found)

                LoadResult.Page(
                    data = vacancies,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }

            is Result.Error -> {
                LoadResult.Error(
                    SearchException(result.error)
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VacancyCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
