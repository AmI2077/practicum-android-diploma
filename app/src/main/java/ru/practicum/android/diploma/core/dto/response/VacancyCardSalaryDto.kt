package ru.practicum.android.diploma.core.dto.response

import com.google.gson.annotations.SerializedName

data class VacancyCardSalaryDto(
    @SerializedName("from")
    val from: Int?,
    @SerializedName("to")
    val to: Int?,
    @SerializedName("currency")
    val currency: String?
)
