package ru.practicum.android.diploma.core.extensions

import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.dto.response.VacancyCardDto
import ru.practicum.android.diploma.core.dto.response.VacancyCardSalaryDto
import ru.practicum.android.diploma.core.models.VacancyCard
import ru.practicum.android.diploma.core.models.VacancyCardSalary
import ru.practicum.android.diploma.core.models.VacancySearchParams

fun VacancySearchParams.toDto(): VacancyRequestDto {
    return VacancyRequestDto(
        area = this.area,
        industry = this.industry,
        text = this.text,
        salary = this.salary,
        page = this.page,
        onlyWithSalary = this.onlyWithSalary
    )
}

fun VacancyCardDto.toModel(): VacancyCard {
    return VacancyCard(
        id = this.id,
        name = this.name,
        company = this.company,
        city = this.city,
        salary = this.salary?.toModel(),
        logo = this.logo
    )
}

fun VacancyCardSalaryDto.toModel(): VacancyCardSalary {
    return VacancyCardSalary(
        from = this.from,
        to = this.to,
        currency = this.currency
    )
}
