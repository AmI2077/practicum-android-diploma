package ru.practicum.android.diploma.feature.filter.data

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.feature.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.feature.filter.domain.models.FilterSettings

class FilterRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : FilterRepository {

    companion object {
        private const val KEY_SALARY = "filter_salary"
        private const val KEY_HIDE_WITHOUT_SALARY = "filter_hide_without_salary"
        private const val KEY_INDUSTRY_ID = "filter_industry_id"
        private const val KEY_INDUSTRY_NAME = "filter_industry_name"
    }

    override fun getFilterSettings(): FilterSettings {
        val salary = sharedPreferences.getInt(KEY_SALARY, 0)
        val hideWithoutSalary = sharedPreferences.getBoolean(KEY_HIDE_WITHOUT_SALARY, false)
        val industryId = sharedPreferences.getInt(KEY_INDUSTRY_ID, -1)
        val industryName = sharedPreferences.getString(KEY_INDUSTRY_NAME, null)

        val industry = if (industryId != -1 && industryName != null) {
            FilterIndustry(
                id = industryId,
                name = industryName
            )
        } else {
            null
        }

        return FilterSettings(
            salary = if (salary > 0) salary else null,
            hideWithoutSalary = hideWithoutSalary,
            industry = industry
        )
    }

    override fun saveFilterSettings(settings: FilterSettings) {
        sharedPreferences.edit {
            putInt(KEY_SALARY, settings.salary ?: 0)
            putBoolean(KEY_HIDE_WITHOUT_SALARY, settings.hideWithoutSalary)
            if (settings.industry != null) {
                putInt(KEY_INDUSTRY_ID, settings.industry.id)
                putString(KEY_INDUSTRY_NAME, settings.industry.name)
            } else {
                remove(KEY_INDUSTRY_ID)
                remove(KEY_INDUSTRY_NAME)
            }
        }
    }

    override fun clearFilterSettings() {
        sharedPreferences.edit {
            remove(KEY_SALARY)
            remove(KEY_HIDE_WITHOUT_SALARY)
            remove(KEY_INDUSTRY_ID)
            remove(KEY_INDUSTRY_NAME)
        }
    }
}
