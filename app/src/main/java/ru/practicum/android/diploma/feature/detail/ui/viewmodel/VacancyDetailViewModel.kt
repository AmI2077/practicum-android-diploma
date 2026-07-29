package ru.practicum.android.diploma.feature.detail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.feature.detail.domain.usecase.GetVacancyDetailUseCase
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractor
import kotlin.time.Duration.Companion.milliseconds


class VacancyDetailViewModel(
    private val getVacancyDetailUseCase: GetVacancyDetailUseCase,
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailState>(VacancyDetailState.Loading)
    val state: StateFlow<VacancyDetailState> = _state.asStateFlow()

    private val _navigationCommand = MutableSharedFlow<VacancyDetailNavigationCommand>()
    val navigationCommand: SharedFlow<VacancyDetailNavigationCommand> = _navigationCommand.asSharedFlow()

    private var clickJob: Job? = null
    private var isClickAllowed = true

    fun loadVacancyDetail(vacancyId: String) {
        _state.value = VacancyDetailState.Loading

        viewModelScope.launch {

            when (val result = getVacancyDetailUseCase(vacancyId)) {
                is Result.Content -> {
                    val vacancy = result.data
                    if (vacancy != null) {
                        val isFavourite = favouritesInteractor.isVacancyFavourite(vacancy.id)
                        _state.value = VacancyDetailState.Content(
                            vacancy = vacancy,
                            isFavourite = isFavourite,
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
                    }
                }
            }
        }
    }

    private fun addToFavourite() {
        val vacancy = (state.value as VacancyDetailState.Content).vacancy

        viewModelScope.launch {
            favouritesInteractor.addVacancyToFavourites(vacancy)
        }
    }

    private fun removeFromFavourite() {
        val vacancy = (state.value as VacancyDetailState.Content).vacancy

        viewModelScope.launch {
            favouritesInteractor.deleteVacancyFromFavourites(vacancy)
        }
    }

    fun onFavouritesClick() {
        val currentState = _state.value as VacancyDetailState.Content

        if (!currentState.isFavourite) {
            addToFavourite()
            _state.value = currentState.copy(
                isFavourite = true
            )
        } else {
            removeFromFavourite()
            _state.value = currentState.copy(
                isFavourite = false
            )
        }
    }

    fun onShareClick(vacancyUrl: String) {
        if (clickDebounce()) {
            viewModelScope.launch {
                _navigationCommand.emit(VacancyDetailNavigationCommand.ShareVacancy(vacancyUrl))
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob?.cancel()
            clickJob = viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY.milliseconds)
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
