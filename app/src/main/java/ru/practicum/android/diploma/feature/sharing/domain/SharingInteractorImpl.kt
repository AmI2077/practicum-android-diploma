package ru.practicum.android.diploma.feature.sharing.domain

import android.content.Intent

class SharingInteractorImpl(
    private val repository: SharingRepository
) : SharingInteractor {

    override fun createShareIntent(vacancyUrl: String): Intent {
        return repository.createShareIntent(vacancyUrl)
    }
}
