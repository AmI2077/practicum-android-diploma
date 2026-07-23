package ru.practicum.android.diploma.feature.filter.ui.utils

import ru.practicum.android.diploma.core.models.filter.FilterIndustry

object IndustryMocks {

    fun getMockIndustries(): List<FilterIndustry> {
        return listOf(
            FilterIndustry(1, "Разработка программного обеспечения"),
            FilterIndustry(2, "Информационные технологии"),
            FilterIndustry(3, "Финансы и банковское дело"),
            FilterIndustry(4, "Маркетинг и реклама"),
            FilterIndustry(5, "Образование и наука"),
            FilterIndustry(6, "Медицина и фармацевтика"),
            FilterIndustry(7, "Строительство и архитектура"),
            FilterIndustry(8, "Транспорт и логистика"),
            FilterIndustry(9, "Торговля и ритейл"),
            FilterIndustry(10, "СМИ и журналистика"),
            FilterIndustry(11, "Юриспруденция"),
            FilterIndustry(12, "Дизайн и креатив"),
            FilterIndustry(13, "Сельское хозяйство"),
            FilterIndustry(14, "Энергетика"),
            FilterIndustry(15, "Телекоммуникации"),
            FilterIndustry(16, "Автомобильная промышленность"),
            FilterIndustry(17, "Гостиничный и ресторанный бизнес"),
            FilterIndustry(18, "Спорт и фитнес"),
            FilterIndustry(19, "Культура и искусство"),
            FilterIndustry(20, "Социальная работа")
        )
    }
}
