package ru.practicum.android.diploma.feature.search.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.extensions.toDto
import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.network.HttpCodes
import ru.practicum.android.diploma.core.network.NetworkResult
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.codeToError
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dispatcher: CoroutineDispatcher
) : SearchRepository {

    override suspend fun fetchVacancies(params: VacancySearchParams): Result<List<VacancyCard>> =
        withContext(dispatcher) {
            when (val result = networkClient.fetchVacancies(params.toDto())) {
                is NetworkResult.Error -> {
                    Result.Error(
                        result.codeToError(result.code)
                    )
                }

                is NetworkResult.Success -> {
                    val vacancies = result.data?.items?.map {
                        it.toModel()
                    } ?: emptyList()

                    Result.Content(vacancies)
                }
            }
        }
}
