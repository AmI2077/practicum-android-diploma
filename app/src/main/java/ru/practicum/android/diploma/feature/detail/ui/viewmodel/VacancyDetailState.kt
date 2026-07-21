package ru.practicum.android.diploma.feature.detail.ui.viewmodel

import ru.practicum.android.diploma.core.models.details.VacancyDetails

sealed class VacancyDetailState {
    object Loading : VacancyDetailState()
//    data class Content(val vacancy: VacancyDetails) : VacancyDetailState()
    data class Content(
    val vacancy: VacancyDetails,
    val isFavourite: Boolean // экран должен знать, добавлена вакансия в избранное
) : VacancyDetailState()
    object Error : VacancyDetailState()
    object NotFound : VacancyDetailState()
    object NoInternet : VacancyDetailState()
}
