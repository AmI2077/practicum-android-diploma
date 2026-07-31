package ru.practicum.android.diploma.feature.search.domain.api

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.search.VacancySearchResult

interface SearchRepository {

    suspend fun fetchVacancies(params: VacancySearchParams): Result<VacancySearchResult>
}
