package ru.practicum.android.diploma.feature.filter.data

import ru.practicum.android.diploma.feature.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings


class FilterRepositoryImpl(
    private val preferences: FilterPreferences
) : FilterRepository {


    override fun saveFilter(
        settings: FilterSettings
    ) {
        preferences.saveFilter(settings)
    }


    override fun getFilter(): FilterSettings {
        return preferences.getFilter()
    }


    override fun clearFilter() {
        preferences.clear()
    }
}
