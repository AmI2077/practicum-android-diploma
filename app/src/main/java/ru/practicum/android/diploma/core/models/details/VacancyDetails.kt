package ru.practicum.android.diploma.core.models.details

import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.Parser.models.FormattedDescription

data class VacancyDetails(
    val id: String,
    val name: String,
    val description: List<FormattedDescription>,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
)


