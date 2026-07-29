package ru.practicum.android.diploma.feature.detail.utils

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.details.Salary
import java.util.Locale

class VacancyDetailFragmentFormatters(
    private val context: Context
) {
    fun formatCurrency(currency: String?): String {
        return when (currency) {
            "RUR", "RUB" -> context.getString(R.string.currency_rub)
            "USD" -> context.getString(R.string.currency_usd)
            "EUR" -> context.getString(R.string.currency_eur)
            "KZT" -> context.getString(R.string.currency_kzt)
            "UAH" -> context.getString(R.string.currency_uah)
            "BYR" -> context.getString(R.string.currency_byr)
            "AZN" -> context.getString(R.string.currency_azn)
            "UZS" -> context.getString(R.string.currency_uzs)
            "GEL" -> context.getString(R.string.currency_gel)
            "KGS" -> context.getString(R.string.currency_kgs)
            else -> ""
        }
    }

    fun formatNumber(number: Int): String {
        return String.format(Locale.getDefault(), "%,d", number).replace(',', ' ')
    }

    fun formatSalary(salary: Salary?): String {
        if (salary == null) {
            return context.getString(R.string.salary_not_specified)
        }

        val from = salary.from
        val to = salary.to
        val currencySymbol = formatCurrency(salary.currency)

        return when {
            from != null && to != null -> {
                "${context.getString(R.string.salary_from)} ${formatNumber(from)} " +
                    "${context.getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
            }

            from != null -> {
                "${context.getString(R.string.salary_from)} ${formatNumber(from)} $currencySymbol"
            }

            to != null -> {
                "${context.getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
            }

            else -> {
                context.getString(R.string.salary_not_specified)
            }
        }
    }
}
