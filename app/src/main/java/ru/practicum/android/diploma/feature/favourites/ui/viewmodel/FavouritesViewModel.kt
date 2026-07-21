package ru.practicum.android.diploma.feature.favourites.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.feature.favourites.domain.interactor.FavouritesInteractor

class FavouritesViewModel(
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val _state =
        MutableLiveData<FavouritesState>()

    val state: LiveData<FavouritesState> = _state


    init {
        loadFavourites()
    }


    private fun loadFavourites() {

        viewModelScope.launch {

            favouritesInteractor
                .getAllFavouritesVacancies()
                .collect { vacancies ->

                    _state.value =
                        if (vacancies.isEmpty()) {
                            FavouritesState.Empty
                        } else {
                            FavouritesState.Content(
                                vacancies
                            )
                        }
                }
        }
    }
}
