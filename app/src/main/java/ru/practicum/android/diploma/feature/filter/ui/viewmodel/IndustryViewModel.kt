package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.models.Result
import ru.practicum.android.diploma.feature.filter.domain.interactor.GetIndustriesInteractor


class IndustryViewModel(

    private val getIndustriesInteractor: GetIndustriesInteractor
): ViewModel() {

    private val _state =
        MutableLiveData<IndustryState>()
    val state: LiveData<IndustryState> = _state

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            _state.value = IndustryState.Loading
            when(
                val result = getIndustriesInteractor()
            ) {

                is Result.Content -> {
                    _state.value =
                        IndustryState.Content(
                            result.data
                        )
                }

                is Result.Error -> {
                    _state.value =
                        IndustryState.Error
                }
            }
        }
    }
}
