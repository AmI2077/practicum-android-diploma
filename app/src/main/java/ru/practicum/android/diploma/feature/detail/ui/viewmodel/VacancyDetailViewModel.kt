package ru.practicum.android.diploma.feature.detail.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractor


class VacancyDetailViewModel(
    private val getVacancyDetailUseCase: GetVacancyDetailUseCase,
    private val favouritesInteractor: FavouritesInteractor

) : ViewModel() {

    private val _state = MutableLiveData<VacancyDetailState>(VacancyDetailState.Loading)
    val state: LiveData<VacancyDetailState> = _state

    private var currentVacancy: VacancyDetails? = null

    fun loadVacancyDetail(
        vacancyId: String
    ) {
        _state.value =
            VacancyDetailState.Loading

        viewModelScope.launch {

            getVacancyDetailUseCase(vacancyId)
                .collect { result ->

                    when(result) {

                        is Result.Content -> {

                            val vacancy = result.data

                            if (vacancy != null) {

                                currentVacancy = vacancy

                                checkFavouriteStatus(vacancy)

                            } else {


                                _state.value =
                                    VacancyDetailState.NotFound

                            }
                        }

                        is Result.Error -> {

                            _state.value =
                                when(result.error) {

                                    NetworkErrors.NoInternetConnectionError ->
                                        VacancyDetailState.NoInternet

                                    NetworkErrors.NotFoundError ->
                                        VacancyDetailState.NotFound

                                    NetworkErrors.ServerError ->
                                        VacancyDetailState.Error
                                }
                        }
                    }
                }
        }
    }
    private fun checkFavouriteStatus(
        vacancy: VacancyDetails
    ) {
        viewModelScope.launch {
            val isFavourite =
                favouritesInteractor
                    .isVacancyFavourite(vacancy.id)
            _state.value =
                VacancyDetailState.Content(
                    vacancy = vacancy,
                    isFavourite = isFavourite
                )
        }
    }

    fun addToFavourite() {

        viewModelScope.launch {
            currentVacancy?.let { vacancy ->
                favouritesInteractor
                    .addVacancyToFavourites(vacancy)
                checkFavouriteStatus(vacancy)
            }
        }
    }

    fun removeFromFavourite() {
        viewModelScope.launch {
            currentVacancy?.let { vacancy ->
                favouritesInteractor
                    .deleteVacancyFromFavourites(vacancy)
                checkFavouriteStatus(vacancy)

            }
        }
    }
}
