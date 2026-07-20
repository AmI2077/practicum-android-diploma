package ru.practicum.android.diploma.feature.detail.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.detail.domain.api.DetailsRepository

class GetVacancyDetailUseCase(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(vacancyId: String): Flow<Result<VacancyDetails?>> = flow {
        emit(repository.fetchVacancyDetails(vacancyId))
    }
}
