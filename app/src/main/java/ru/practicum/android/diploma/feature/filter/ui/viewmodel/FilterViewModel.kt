package ru.practicum.android.diploma.feature.filter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.core.models.filter.FilterIndustry

class FilterViewModel(): ViewModel() {

    private val _state = MutableLiveData(FilterState())
    val state: LiveData<FilterState> = _state

    fun saveSalary(value: Int?) {
        updateState(
            salary = value
        )
    }

    fun saveIndustry(filterIndustry: FilterIndustry?) {
        updateState(
            industry = filterIndustry
        )
    }

    fun saveHideWithoutSalary(
        checked: Boolean
    ) {
        updateState(
            hideWithoutSalary = checked
        )
    }

    fun clearFilter() {
        updateState(
            salary = 0,
            hideWithoutSalary = false,
            industry = null
        )
    }

    private fun updateState(
        salary: Int? = null,
        hideWithoutSalary: Boolean? = null,
        industry: FilterIndustry? = null
    ) {
        val currentState = _state.value ?: return

        _state.value = currentState.copy(
            salary = salary ?: currentState.salary,
            hideWithoutSalary = hideWithoutSalary ?: currentState.hideWithoutSalary,
            industry = industry ?: currentState.industry
        )
    }
}
