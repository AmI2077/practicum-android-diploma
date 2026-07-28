package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import ru.practicum.android.diploma.core.models.filter.FilterIndustry

data class FilterState(
    val salary: Int? = null,
    val hideWithoutSalary: Boolean = false,
    val industry: FilterIndustry? = null
)
