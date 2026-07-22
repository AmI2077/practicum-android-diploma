package ru.practicum.android.diploma.feature.detail.ui.viewmodel

sealed class VacancyDetailNavigationCommand {
    data class ShareVacancy(val url: String) : VacancyDetailNavigationCommand()
}
