package ru.practicum.android.diploma.feature.search.ui.utils

import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.card.VacancyCardSalary

object MockData {
    fun getMockVacancies(): List<VacancyCard> {
        return listOf(
            VacancyCard(
                id = "1",
                name = "Senior Android Developer",
                company = "Google Россия",
                city = null,
                salary = VacancyCardSalary(
                    from = 300,
                    to = 400,
                    currency = "RUB"
                ),
                logo = null
            ),
            VacancyCard(
                id = "2",
                name = "Ведущий разработчик мобильных приложений на Kotlin для крупного финтех-проекта",
                company = "Тинькофф",
                city = "Санкт-Петербург",
                salary = VacancyCardSalary(
                    from = null,
                    to = 350,
                    currency = "RUB"
                ),
                logo = null
            )
        )
    }
}
