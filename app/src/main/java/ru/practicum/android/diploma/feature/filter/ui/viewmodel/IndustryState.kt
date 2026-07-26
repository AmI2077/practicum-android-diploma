package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import ru.practicum.android.diploma.core.models.filter.FilterIndustry

sealed interface IndustryState {

    data object Loading : IndustryState

    data class Content(
        val industries: List<FilterIndustry>
    ): IndustryState

    data object Error : IndustryState


}
