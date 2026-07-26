package ru.practicum.android.diploma.feature.filter.domain.api

import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

interface FilterRepository {

    fun getFilterSettings(): FilterSettings

    fun saveFilterSettings(settings: FilterSettings)

    fun clearFilterSettings()
}
