package ru.practicum.android.diploma.feature.search.data

import ru.practicum.android.diploma.core.models.NetworkErrors

class SearchException(
    val networkError: NetworkErrors
) : Exception()
