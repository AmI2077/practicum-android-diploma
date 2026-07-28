package ru.practicum.android.diploma.feature.detail.domain.usecase

import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.detail.domain.api.DetailsRepository

class GetVacancyDetailUseCase(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(
        vacancyId: String
    ): Result<VacancyDetails?> {

        return repository.fetchVacancyDetails(vacancyId)
    }
}
