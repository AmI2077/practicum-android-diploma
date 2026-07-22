package ru.practicum.android.diploma.feature.sharing.data


import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.sharing.domain.SharingRepository

class SharingRepositoryImpl(
    private val context: Context
) : SharingRepository {

    override fun createShareIntent(vacancyUrl: String): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, vacancyUrl)
        }

        return Intent.createChooser(
            shareIntent,
            context.getString(R.string.share_vacancy)
        )
    }
}
