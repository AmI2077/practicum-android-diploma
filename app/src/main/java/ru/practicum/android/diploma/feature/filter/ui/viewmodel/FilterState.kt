package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import ru.practicum.android.diploma.core.models.filter.FilterIndustry

sealed interface FilterState {

    data class Content(
        val salary: String,
        val hideWithoutSalary: Boolean,
        val industry: FilterIndustry?
    ): FilterState

}
