package ru.practicum.android.diploma.core.extensions

import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.models.VacancySearchParams
import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.detail.data.dto.FilterAreaDto
import ru.practicum.android.diploma.feature.filter.data.dto.FilterIndustryDto

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
