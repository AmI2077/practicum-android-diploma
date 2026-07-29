package ru.practicum.android.diploma.feature.search.domain.usecase

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.search.VacancySearchResult
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository

class SearchVacanciesUseCase(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(
        params: VacancySearchParams
    ): Result<VacancySearchResult> {

        return repository.fetchVacancies(params)
    }
}
