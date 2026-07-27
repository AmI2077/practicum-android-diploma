package ru.practicum.android.diploma.core.models.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterIndustry(
    val id: Int,
    val name: String,
): Parcelable
