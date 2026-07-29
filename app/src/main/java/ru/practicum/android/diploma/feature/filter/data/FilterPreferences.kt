package ru.practicum.android.diploma.feature.filter.data

import android.content.SharedPreferences
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

class FilterPreferences(
    private val prefs: SharedPreferences
) {

    fun saveFilter(settings: FilterSettings) {
        prefs.edit()
            .putInt(
                SALARY,
                settings.salary ?: -1
            )
            .putBoolean(
                HIDE_WITHOUT_SALARY,
                settings.hideWithoutSalary
            )
            .putInt(
                INDUSTRY_ID,
                settings.industry?.id ?: -1
            )
            .putString(
                INDUSTRY_NAME,
                settings.industry?.name
            )
            .apply()
    }


    fun getFilter(): FilterSettings {

        val salary =
            prefs.getInt(SALARY, -1)
                .takeIf { it != -1 }


        val industryId =
            prefs.getInt(INDUSTRY_ID, -1)


        val industry =
            if (industryId != -1) {
                FilterIndustry(
                    id = industryId,
                    name = prefs.getString(
                        INDUSTRY_NAME,
                        ""
                    ).orEmpty()
                )
            } else {
                null
            }


        return FilterSettings(
            salary = salary,
            hideWithoutSalary =
                prefs.getBoolean(
                    HIDE_WITHOUT_SALARY,
                    false
                ),
            industry = industry
        )
    }


    fun clear() {
        prefs.edit()
            .clear()
            .apply()
    }


    companion object {
        private const val SALARY = "salary"
        private const val HIDE_WITHOUT_SALARY =
            "hide_without_salary"
        private const val INDUSTRY_ID =
            "industry_id"
        private const val INDUSTRY_NAME =
            "industry_name"
    }
}
