package ru.practicum.android.diploma.feature.search.domain.api

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.VacancySearchParams

interface SearchRepository {

    suspend fun fetchVacancies(params: VacancySearchParams): Result<List<VacancyCard>>
}
