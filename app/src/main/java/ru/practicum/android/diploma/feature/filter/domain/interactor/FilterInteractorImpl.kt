package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.feature.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

class FilterInteractorImpl(
    private val repository: FilterRepository
): FilterInteractor {


    override fun getSettings(): FilterSettings {
        return repository.getFilterSettings()
    }


    override fun saveSettings(
        settings: FilterSettings
    ) {
        repository.saveFilterSettings(settings)
    }


    override fun clearSettings() {
        repository.clearFilterSettings()
    }

}
