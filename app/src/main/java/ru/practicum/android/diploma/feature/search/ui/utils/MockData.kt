package ru.practicum.android.diploma.feature.search.ui.utils

import ru.practicum.android.diploma.core.models.VacancyCard
import ru.practicum.android.diploma.core.models.VacancyCardSalary

object MockData {

    fun getMockVacancies(): List<VacancyCard> {
        return listOf(
            // 1. Полные данные
            VacancyCard(
                id = "1",
                name = "Senior Android Developer",
                company = "Google Россия",
                city = "Москва",
                salary = VacancyCardSalary(
                    from = 300000,
                    to = 400000,
                    currency = "RUB"
                ),
                logo = null
            ),

            // 2. Длинное название (проверка переноса)
            VacancyCard(
                id = "2",
                name = "Ведущий разработчик мобильных приложений на Kotlin для крупного финтех-проекта",
                company = "Тинькофф",
                city = "Санкт-Петербург",
                salary = VacancyCardSalary(
                    from = 250000,
                    to = 350000,
                    currency = "RUB"
                ),
                logo = null
            ),

            // 3. Только "от", город отсутствует
            VacancyCard(
                id = "3",
                name = "Product Manager",
                company = "Яндекс",
                city = null,
                salary = VacancyCardSalary(
                    from = 200000,
                    to = null,
                    currency = "USD"
                ),
                logo = null
            ),

            // 4. Только "до", компания отсутствует
            VacancyCard(
                id = "4",
                name = "UX/UI Designer",
                company = null,
                city = "Казань",
                salary = VacancyCardSalary(
                    from = null,
                    to = 180000,
                    currency = "RUB"
                ),
                logo = null
            ),

            // 5. Зарплата не указана (salary == null)
            VacancyCard(
                id = "5",
                name = "Data Scientist",
                company = "Сбербанк",
                city = "Москва",
                salary = null, // Зарплата полностью отсутствует
                logo = null
            ),

            // 6. С логотипом (проверка загрузки изображения)
            VacancyCard(
                id = "6",
                name = "iOS Developer",
                company = "Apple",
                city = "Санкт-Петербург",
                salary = VacancyCardSalary(
                    from = 280000,
                    to = 380000,
                    currency = "RUB"
                ),
                logo = "https://example.com/apple_logo.png" // Реальный URL не важен, будет плейсхолдер
            ),

            // 7. С валютой EUR
            VacancyCard(
                id = "7",
                name = "Frontend Developer",
                company = "EPAM Systems",
                city = "Минск",
                salary = VacancyCardSalary(
                    from = 4000,
                    to = 6000,
                    currency = "EUR"
                ),
                logo = null
            ),

            // 8. Только "от" с валютой KZT (тенге)
            VacancyCard(
                id = "8",
                name = "Backend Developer",
                company = "Kaspi.kz",
                city = "Алматы",
                salary = VacancyCardSalary(
                    from = 500000,
                    to = null,
                    currency = "KZT"
                ),
                logo = null
            ),

            // 9. Без города
            VacancyCard(
                id = "9",
                name = "Backend Developer",
                company = "Kaspi.kz",
                city = null,
                salary = VacancyCardSalary(
                    from = 500000,
                    to = null,
                    currency = "KZT"
                ),
                logo = null
            ),

            // 10. Только "до" с валютой UAH (гривна)
            VacancyCard(
                id = "10",
                name = "QA Engineer",
                company = "EPAM Ukraine",
                city = "Киев",
                salary = VacancyCardSalary(
                    from = null,
                    to = 3000,
                    currency = "UAH"
                ),
                logo = null
            )


        )
    }
}
