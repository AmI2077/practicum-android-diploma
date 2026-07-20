package ru.practicum.android.diploma.feature.detail.domain.api

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails

interface DetailsRepository {

    suspend fun fetchVacancyDetails(vacancyId: String): Result<VacancyDetails?>

    suspend fun addVacancyToFavourites(vacancyDetails: VacancyDetails)

    suspend fun deleteVacancyFromFavourites(vacancyDetails: VacancyDetails)
}
