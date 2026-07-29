package ru.practicum.android.diploma.feature.search.ui

sealed interface PagingUiState {
    object Initial : PagingUiState
    object Loading : PagingUiState
    object Success : PagingUiState
    object Empty : PagingUiState
    data class Error(val error: Throwable) : PagingUiState
}
