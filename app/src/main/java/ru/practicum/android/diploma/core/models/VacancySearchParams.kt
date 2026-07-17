package ru.practicum.android.diploma.core.models

data class VacancySearchParams(
    val area: Int? = null,
    val industry: Int? = null,
    val text: String,
    val salary: Int? = null,
    val page: Int? = null,
    val onlyWithSalary: Boolean? = null,
)
