package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

fun FilterSettings.toFilterState(): FilterState {
    return FilterState(
        salary = salary,
        hideWithoutSalary = hideWithoutSalary,
        industry = industry
    )
}
