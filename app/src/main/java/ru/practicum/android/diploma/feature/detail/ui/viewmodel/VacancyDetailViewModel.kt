package ru.practicum.android.diploma.feature.detail.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _navigationCommand = MutableLiveData<Event<VacancyDetailNavigationCommand>>()
    val navigationCommand: LiveData<Event<VacancyDetailNavigationCommand>> = _navigationCommand

    private var currentVacancy: VacancyDetails? = null

    fun loadVacancyDetail(vacancyId: String) {
        _state.value = VacancyDetailState.Loading

        viewModelScope.launch {
            val result = getVacancyDetailUseCase(vacancyId)

            when (result) {
                is Result.Content -> {
                    val vacancy = result.data

                    if (vacancy != null) {
                        currentVacancy = vacancy

                        val isFavourite = favouritesInteractor.isVacancyFavourite(vacancy.id)

                        _state.value = VacancyDetailState.Content(
                            vacancy = vacancy,
                            isFavourite = isFavourite
                        )
                    } else {
                        _state.value = VacancyDetailState.NotFound
                    }
                }

                is Result.Error -> {
                    _state.value = when (result.error) {
                        NetworkErrors.NoInternetConnectionError -> VacancyDetailState.NoInternet
                        NetworkErrors.NotFoundError -> VacancyDetailState.NotFound
                        NetworkErrors.ServerError -> VacancyDetailState.Error
    private var clickJob: Job? = null
    private var isClickAllowed = true

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
    }


    fun addToFavourite() {
        currentVacancy?.let { vacancy ->
            viewModelScope.launch {
                favouritesInteractor.addVacancyToFavourites(vacancy)

                // Перепроверяем статус, чтобы UI был консистентным
                val isFavourite = favouritesInteractor.isVacancyFavourite(vacancy.id)

                _state.value = (state.value as? VacancyDetailState.Content)?.copy(isFavourite = isFavourite)
            }
        }
    }

    fun removeFromFavourite() {
        currentVacancy?.let { vacancy ->
            viewModelScope.launch {
                favouritesInteractor.deleteVacancyFromFavourites(vacancy)

                val isFavourite = favouritesInteractor.isVacancyFavourite(vacancy.id)

                _state.value = (state.value as? VacancyDetailState.Content)?.copy(isFavourite = isFavourite)
            }
        }
    }

    fun onShareClick(vacancyUrl: String) {
        if (clickDebounce()) {
            _navigationCommand.value = Event(
                VacancyDetailNavigationCommand.ShareVacancy(vacancyUrl)
            )
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob?.cancel()
            clickJob = viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onCleared() {
        clickJob?.cancel()
        super.onCleared()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}
