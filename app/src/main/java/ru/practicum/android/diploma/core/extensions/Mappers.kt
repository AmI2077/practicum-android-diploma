package ru.practicum.android.diploma.core.extensions

import ru.practicum.android.diploma.core.database.entities.VacancyEntity
import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.dto.response.VacancyCardDto
import ru.practicum.android.diploma.core.dto.response.VacancyCardSalaryDto
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.card.VacancyCardSalary
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.details.Address
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Employer
import ru.practicum.android.diploma.core.models.details.Employment
import ru.practicum.android.diploma.core.models.details.Experience
import ru.practicum.android.diploma.core.models.details.Phone
import ru.practicum.android.diploma.core.models.details.Salary
import ru.practicum.android.diploma.core.models.details.Schedule
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.core.models.search.VacancySearchResult
import ru.practicum.android.diploma.feature.detail.data.dto.AddressDto
import ru.practicum.android.diploma.feature.detail.data.dto.ContactsDto
import ru.practicum.android.diploma.feature.detail.data.dto.EmployerDto
import ru.practicum.android.diploma.feature.detail.data.dto.EmploymentDto
import ru.practicum.android.diploma.feature.detail.data.dto.ExperienceDto
import ru.practicum.android.diploma.feature.detail.data.dto.FilterAreaDto
import ru.practicum.android.diploma.feature.detail.data.dto.PhoneDto
import ru.practicum.android.diploma.feature.detail.data.dto.SalaryDto
import ru.practicum.android.diploma.feature.detail.data.dto.ScheduleDto
import ru.practicum.android.diploma.feature.detail.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.feature.filter.data.dto.FilterIndustryDto

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

fun SalaryDto.toModel() = Salary(
    from = this.from,
    to = this.to,
    currency = this.currency
)

fun AddressDto.toModel() = Address(
    id = this.id,
    city = this.city,
    street = this.street,
    building = this.building,
    raw = this.raw
)

fun ExperienceDto.toModel() = Experience(
    id = this.id,
    name = this.name
)

fun ScheduleDto.toModel() = Schedule(
    id = this.id,
    name = this.name
)

fun EmploymentDto.toModel() = Employment(
    id = this.id,
    name = this.name
)

fun ContactsDto.toModel() = Contacts(
    id = this.id,
    name = this.name,
    email = this.email,
    phones = this.phones.map { it.toModel() }
)

fun PhoneDto.toModel() = Phone(
    comment = this.comment,
    formatted = this.formatted
)

fun EmployerDto.toModel() = Employer(
    id = this.id,
    name = this.name,
    logo = this.logo
)

fun FilterAreaDto.toModel(): FilterArea = FilterArea(
    id = this.id,
    name = this.name,
    parentId = this.parentId,
    areas = this.areas.map { it.toModel() }
)


fun FilterIndustryDto.toModel(): FilterIndustry = FilterIndustry(
    id = this.id,
    name = this.name
)

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
