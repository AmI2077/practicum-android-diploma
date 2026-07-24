package ru.practicum.android.diploma.feature.search.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.extensions.toDto
import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.search.VacancySearchResult
import ru.practicum.android.diploma.core.network.NetworkResult
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.codeToError
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dispatcher: CoroutineDispatcher
) : SearchRepository {

    override suspend fun fetchVacancies(params: VacancySearchParams): Result<VacancySearchResult>{
    return withContext(dispatcher) {
            when (val result = networkClient.fetchVacancies(params.toDto())) {
                is NetworkResult.Error -> {
                    Result.Error(
                        result.codeToError()
                    )
                }
                is NetworkResult.Success -> {

                    val response = result.data

                    if (response == null) {
                        Result.Error(NetworkErrors.ServerError)

                    } else {
                        Result.Content(
                            response.toModel()
                        )
                    }
                }
            }
        }
    }
}
