package ru.practicum.android.diploma.feature.detail.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.feature.filter.data.dto.FilterIndustryDto

data class VacancyDetailsDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("salary") val salary: SalaryDto?,
    @SerializedName("address") val address: AddressDto?,
    @SerializedName("experience") val experience: ExperienceDto?,
    @SerializedName("schedule") val schedule: ScheduleDto?,
    @SerializedName("employment") val employment: EmploymentDto?,
    @SerializedName("contacts") val contacts: ContactsDto?,
    @SerializedName("employer") val employer: EmployerDto,
    @SerializedName("area") val area: FilterAreaDto,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("industry") val industry: FilterIndustryDto
)

data class SalaryDto(
    @SerializedName("from") val from: Int?,
    @SerializedName("to") val to: Int?,
    @SerializedName("currency") val currency: String?
)

data class AddressDto(
    @SerializedName("id") val id: String,
    @SerializedName("city") val city: String,
    @SerializedName("street") val street: String,
    @SerializedName("building") val building: String,
    @SerializedName("raw") val raw: String
)

data class ExperienceDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class ScheduleDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class EmploymentDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class ContactsDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phones") val phones: List<PhoneDto>
)

data class PhoneDto(
    @SerializedName("comment") val comment: String?,
    @SerializedName("formatted") val formatted: String
)

data class EmployerDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String
)

data class FilterAreaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("parentId") val parentId: Int,
    @SerializedName("areas") val areas: List<FilterAreaDto>
)
