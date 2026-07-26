package ru.practicum.android.diploma.feature.filter.domain.interactor

import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings


interface FilterInteractor {

    fun getSettings(): FilterSettings

    fun saveSettings(settings: FilterSettings)

    fun clearSettings()

}
