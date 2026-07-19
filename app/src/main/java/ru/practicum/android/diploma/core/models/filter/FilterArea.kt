package ru.practicum.android.diploma.core.models.filter

data class FilterArea(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<FilterArea>
)
