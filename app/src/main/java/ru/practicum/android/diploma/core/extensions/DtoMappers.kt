package ru.practicum.android.diploma.core.extensions

import ru.practicum.android.diploma.core.models.details.Address
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Employer
import ru.practicum.android.diploma.core.models.details.Employment
import ru.practicum.android.diploma.core.models.details.Experience
import ru.practicum.android.diploma.core.models.details.Phone
import ru.practicum.android.diploma.core.models.details.Salary
import ru.practicum.android.diploma.core.models.details.Schedule
import ru.practicum.android.diploma.feature.detail.data.dto.AddressDto
import ru.practicum.android.diploma.feature.detail.data.dto.ContactsDto
import ru.practicum.android.diploma.feature.detail.data.dto.EmployerDto
import ru.practicum.android.diploma.feature.detail.data.dto.EmploymentDto
import ru.practicum.android.diploma.feature.detail.data.dto.ExperienceDto
import ru.practicum.android.diploma.feature.detail.data.dto.PhoneDto
import ru.practicum.android.diploma.feature.detail.data.dto.SalaryDto
import ru.practicum.android.diploma.feature.detail.data.dto.ScheduleDto

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
