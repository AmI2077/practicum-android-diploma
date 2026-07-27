package ru.practicum.android.diploma.feature.filter.data

import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.core.network.client.NetworkClient
import ru.practicum.android.diploma.feature.filter.domain.api.IndustryRepository

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient
) : IndustryRepository {

    override suspend fun getIndustries(): Result<List<FilterIndustry>> {
        return try {
            // Пока возвращаем заглушку
            Result.Content(getMockIndustries())
        } catch (e: Exception) {
            Result.Error(NetworkErrors.NoInternetConnectionError)
        }
    }

    private fun getMockIndustries(): List<FilterIndustry> {
        return listOf(
            FilterIndustry(1, "IT и разработка"),
            FilterIndustry(2, "Финансы и банки"),
            FilterIndustry(3, "Маркетинг и реклама"),
            FilterIndustry(4, "Образование"),
            FilterIndustry(5, "Здравоохранение"),
            FilterIndustry(6, "Мы котики")
        )
    }
}
