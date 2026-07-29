package ru.practicum.android.diploma.feature.filter.domain.api

import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

interface FilterRepository {

    fun saveFilter(settings: FilterSettings)

    fun getFilter(): FilterSettings

    fun clearFilter()
}
