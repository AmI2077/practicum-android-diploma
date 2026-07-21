package ru.practicum.android.diploma.feature.sharing.domain

import android.content.Intent

interface SharingInteractor {
    fun createShareIntent(vacancyUrl: String): Intent
}
