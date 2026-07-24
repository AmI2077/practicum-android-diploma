package ru.practicum.android.diploma.feature.detail.data.repository

import androidx.core.text.HtmlCompat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.extensions.toEntity
import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.core.network.NetworkResult
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.codeToError
import ru.practicum.android.diploma.feature.detail.domain.api.DetailsRepository

class DetailsRepositoryImpl(
    private val networkClient: NetworkClient,
//    private val vacancyDao: VacancyDao,
    private val dispatcher: CoroutineDispatcher,
): DetailsRepository {

    override suspend fun fetchVacancyDetails(
        vacancyId: String
    ): Result<VacancyDetails?> {
        return withContext(dispatcher) {
            when(
                val result = networkClient.fetchVacancyDetails(vacancyId)
            ) {
                is NetworkResult.Error -> {
                    Result.Error(
                        result.codeToError()
                    )
                }
                is NetworkResult.Success -> {
                    result.data?.let {
                        val description = parseHtml(it.description)
                        Result.Content(
                            data = result.data.copy(
                                description = description
                            ).toModel()
                        )
                    } ?: Result.Content(null)
                }
            }
        }
    }

    private fun parseHtml(raw: String): String {
        return HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    }
}

