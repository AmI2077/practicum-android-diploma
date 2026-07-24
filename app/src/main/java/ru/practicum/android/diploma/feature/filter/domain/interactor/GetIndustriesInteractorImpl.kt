package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.filter.domain.api.IndustryRepository


class GetIndustriesInteractorImpl(
    private val repository: IndustryRepository
): GetIndustriesInteractor {


    override suspend fun invoke():
        Result<List<FilterIndustry>> {

        return repository.getIndustries()

    }

}
