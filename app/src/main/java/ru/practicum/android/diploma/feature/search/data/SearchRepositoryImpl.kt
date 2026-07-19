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
                        handleErrorCode(result.code)
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

    private fun handleErrorCode(code: HttpCodes): NetworkErrors {
        return when (code) {
            HttpCodes.NO_INTERNET_CONNECTION_ERROR_CODE -> NetworkErrors.NoInternetConnectionError
            HttpCodes.SERVER_ERROR_CODE -> NetworkErrors.ServerError
            HttpCodes.NOT_FOUND_ERROR_CODE -> NetworkErrors.NotFoundError
        }
    }
}
