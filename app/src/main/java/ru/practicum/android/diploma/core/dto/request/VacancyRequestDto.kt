package ru.practicum.android.diploma.core.dto.request

data class VacancyRequestDto(
    val area: Int? = null,
    val industry: Int? = null,
    val text: String,
    val salary: Int? = null,
    val page: Int? = null,
    val onlyWithSalary: Boolean? = null,
)
