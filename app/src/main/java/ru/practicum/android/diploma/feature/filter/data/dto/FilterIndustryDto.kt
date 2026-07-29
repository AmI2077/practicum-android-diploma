package ru.practicum.android.diploma.feature.filter.data.dto

import com.google.gson.annotations.SerializedName

class FilterIndustryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
