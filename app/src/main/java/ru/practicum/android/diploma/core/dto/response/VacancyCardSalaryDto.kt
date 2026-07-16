package ru.practicum.android.diploma.core.dto.response

import com.google.gson.annotations.SerializedName

data class VacancyCardSalaryDto(
    @SerializedName("from")
    val from: String?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("currency")
    val currency: String?
)
