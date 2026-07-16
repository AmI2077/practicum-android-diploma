package ru.practicum.android.diploma.core.dto

import com.google.gson.annotations.SerializedName

data class VacancyCardDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("company")
    val company: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("salary")
    val salary: VacancyCardSalaryDto?,
    @SerializedName("logo")
    val logo: String?,
)
