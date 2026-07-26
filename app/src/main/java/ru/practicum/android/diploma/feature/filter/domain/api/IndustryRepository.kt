package ru.practicum.android.diploma.feature.filter.domain.api

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.filter.FilterIndustry


interface IndustryRepository {

    suspend fun getIndustries():
        Result<List<FilterIndustry>>

}
