package ru.practicum.android.diploma.feature.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase

sealed class VacancyDetailState {
    object Loading : VacancyDetailState()
    data class Content(val vacancy: VacancyDetails) : VacancyDetailState()
    object Error : VacancyDetailState()
    object NotFound : VacancyDetailState()
    object NoInternet : VacancyDetailState()
}

class VacancyDetailViewModel(
    private val getVacancyDetailUseCase: GetVacancyDetailUseCase
) : ViewModel() {

    private val _state = MutableLiveData<VacancyDetailState>(VacancyDetailState.Loading)
    val state: LiveData<VacancyDetailState> = _state

    fun loadVacancyDetail(vacancyId: String) {

        val testId = "0000258d-fb45-3152-bfeb-250a4c547384" // подменяем id удалить

        _state.value = VacancyDetailState.Loading

        viewModelScope.launch {
            getVacancyDetailUseCase(testId).collect { result -> // удалить когда будем получать данные с сервера
            // getVacancyDetailUseCase(vacancyId).collect { result -> для передачи id с сервера
                when (result) {
                    is Result.Content -> {
                        if (result.data != null) {
                            _state.value = VacancyDetailState.Content(result.data)
                        } else {
                            _state.value = VacancyDetailState.NotFound
                        }
                    }
                    is Result.Error -> {
                        _state.value = when (result.error) {
                            NetworkErrors.NoInternetConnectionError -> VacancyDetailState.NoInternet
                            NetworkErrors.NotFoundError -> VacancyDetailState.NotFound
                            NetworkErrors.ServerError -> VacancyDetailState.Error
                        }
                    }
                }
            }
        }
    }
}
