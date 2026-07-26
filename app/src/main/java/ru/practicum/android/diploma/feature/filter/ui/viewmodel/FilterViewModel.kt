package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.feature.filter.domain.interactor.FilterInteractor

class FilterViewModel(
    private val filterInteractor: FilterInteractor
): ViewModel() {


    private val _state =
        MutableLiveData<FilterState>()

    val state: LiveData<FilterState> = _state

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val settings =
            filterInteractor.getSettings()
        _state.value =
            FilterState.Content(
                salary = settings.salary?.toString().orEmpty(),
                hideWithoutSalary = settings.hideWithoutSalary,
                industry = settings.industry
            )
    }

    fun saveSalary(value: Int?) {
        val current =
            filterInteractor.getSettings()
        filterInteractor.saveSettings(
            current.copy(
                salary = value
            )
        )
    }

    fun saveHideWithoutSalary(
        checked: Boolean
    ) {
        val current =
            filterInteractor.getSettings()
        filterInteractor.saveSettings(
            current.copy(
                hideWithoutSalary = checked
            )
        )
    }

    fun clearFilter() {
        filterInteractor.clearSettings()
        loadSettings()
    }
}
