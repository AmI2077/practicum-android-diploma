package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

interface FilterInteractor {

    fun save(settings: FilterSettings)

    fun get(): FilterSettings

    fun clear()
}
