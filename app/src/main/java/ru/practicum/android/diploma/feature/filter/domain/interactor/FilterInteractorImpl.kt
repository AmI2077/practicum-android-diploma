package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.feature.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

class FilterInteractorImpl(
    private val repository: FilterRepository
) : FilterInteractor {


    override fun save(
        settings: FilterSettings
    ) {
        repository.saveFilter(settings)
    }


    override fun get(): FilterSettings {
        return repository.getFilter()
    }


    override fun clear() {
        repository.clearFilter()
    }
}
