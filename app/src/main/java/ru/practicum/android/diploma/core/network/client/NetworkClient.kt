package ru.practicum.android.diploma.core.network.client

import ru.practicum.android.diploma.core.dto.request.VacancyRequestDto
import ru.practicum.android.diploma.core.dto.response.VacancyResponseDto
import ru.practicum.android.diploma.core.network.NetworkResult

interface NetworkClient {

    suspend fun fetchVacancies(vacancyRequestDto: VacancyRequestDto): NetworkResult<VacancyResponseDto?>
}
