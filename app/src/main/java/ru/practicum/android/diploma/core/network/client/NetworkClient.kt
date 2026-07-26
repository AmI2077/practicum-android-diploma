package ru.practicum.android.diploma.core.network.client

import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto
import ru.practicum.android.diploma.core.network.NetworkResult
import ru.practicum.android.diploma.feature.detail.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.feature.filter.data.dto.IndustryDto

interface NetworkClient {

    suspend fun fetchVacancies(vacancyRequestDto: VacancyRequestDto): NetworkResult<VacancyResponseDto?>

    suspend fun fetchVacancyDetails(vacancyId: String): NetworkResult<VacancyDetailsDto?>

//    suspend fun fetchIndustries(): NetworkResult<List<IndustryDto>>
}
