package ru.practicum.android.diploma.feature.filter.data

import ru.practicum.android.diploma.core.extensions.toModel
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.core.network.NetworkResult
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.core.network.codeToError
import ru.practicum.android.diploma.feature.filter.domain.api.IndustryRepository

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient
) : IndustryRepository {

    override suspend fun getIndustries(): Result<List<FilterIndustry>> {
        return when (val result = networkClient.fetchIndustries()) {
            is NetworkResult.Error -> {
                Result.Error(result.codeToError())
            }

            is NetworkResult.Success -> {
                val industries = result.data?.map {
                    it.toModel()
                } ?: emptyList()
                Result.Content(industries)
            }
        }
    }
}

