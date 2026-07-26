package ru.practicum.android.diploma.core.models.details

import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.card.VacancyCardSalary
import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry

data class VacancyDetails(
    val id: String,
    val name: String,
    val description: String,
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

fun VacancyDetails.toCard(): VacancyCard {
    return VacancyCard(
        id = this.id,
        name = this.name,
        company = this.employer.name,
        city = this.address?.city,
        salary = this.salary?.toVacancyCardSalary(),
        logo = this.employer.logo
    )
}

fun Salary.toVacancyCardSalary(): VacancyCardSalary {
    return VacancyCardSalary(
        from = this.from,
        to = this.to,
        currency = this.currency
    )
}
