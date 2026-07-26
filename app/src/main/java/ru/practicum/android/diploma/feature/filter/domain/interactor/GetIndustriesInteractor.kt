package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.filter.FilterIndustry


interface GetIndustriesInteractor {

    suspend operator fun invoke():
        Result<List<FilterIndustry>>

}
