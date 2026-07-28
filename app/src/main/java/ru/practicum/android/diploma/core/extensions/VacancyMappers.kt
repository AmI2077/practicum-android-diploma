package ru.practicum.android.diploma.core.extensions

import ru.practicum.android.diploma.core.database.entities.VacancyEntity
import ru.practicum.android.diploma.core.dto.response.VacancyCardDto
import ru.practicum.android.diploma.core.dto.response.VacancyCardSalaryDto
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.card.VacancyCardSalary
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.core.models.search.VacancySearchResult
import ru.practicum.android.diploma.feature.detail.data.dto.VacancyDetailsDto

fun VacancyDetails.toEntity(): VacancyEntity {
    return VacancyEntity(
        id = id,
        name = name,
        description = description,
        salary = salary,
        address = address,
        experience = experience,
        schedule = schedule,
        employment = employment,
        contacts = contacts,
        employer = employer,
        area = area,
        skills = skills,
        url = url,
        industry = industry
    )
}

fun VacancyEntity.toModel(): VacancyDetails {
    return VacancyDetails(
        id = id,
        name = name,
        description = description,
        salary = salary,
        address = address,
        experience = experience,
        schedule = schedule,
        employment = employment,
        contacts = contacts,
        employer = employer,
        area = area,
        skills = skills,
        url = url,
        industry = industry
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

fun VacancyDetailsDto.toModel(): VacancyDetails {
    return VacancyDetails(
        id = this.id,
        name = this.name,
        description = this.description,
        salary = this.salary?.toModel(),
        address = this.address?.toModel(),
        experience = this.experience?.toModel(),
        schedule = this.schedule?.toModel(),
        employment = this.employment?.toModel(),
        contacts = this.contacts?.toModel(),
        employer = this.employer.toModel(),
        area = this.area.toModel(),
        skills = this.skills,
        url = this.url,
        industry = this.industry.toModel()
    )
}

fun VacancyResponseDto.toModel(): VacancySearchResult {

    return VacancySearchResult(
        vacancies = items.map {
            it.toModel()
        },
        found = found,
        page = page,
        pages = pages
    )
}
