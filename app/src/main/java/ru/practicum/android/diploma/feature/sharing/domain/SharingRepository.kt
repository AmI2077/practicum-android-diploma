package ru.practicum.android.diploma.feature.sharing.domain

import android.content.Intent

interface SharingRepository {
    fun createShareIntent(vacancyUrl: String): Intent
}
