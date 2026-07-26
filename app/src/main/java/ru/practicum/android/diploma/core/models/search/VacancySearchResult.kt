package ru.practicum.android.diploma.core.models.search

import ru.practicum.android.diploma.core.models.card.VacancyCard

data class VacancySearchResult(
    val vacancies: List<VacancyCard>,
    val found: Int,
    val page: Int,
    val pages: Int
)
