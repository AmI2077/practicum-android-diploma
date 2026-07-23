package ru.practicum.android.diploma.feature.filter.ui.utils

import ru.practicum.android.diploma.core.models.filter.FilterIndustry

object IndustryMocks {

    private const val ID_SOFTWARE = 1
    private const val ID_IT = 2
    private const val ID_FINANCE = 3
    private const val ID_MARKETING = 4
    private const val ID_EDUCATION = 5
    private const val ID_MEDICINE = 6
    private const val ID_CONSTRUCTION = 7
    private const val ID_TRANSPORT = 8
    private const val ID_RETAIL = 9
    private const val ID_MEDIA = 10
    private const val ID_LAW = 11
    private const val ID_DESIGN = 12
    private const val ID_AGRICULTURE = 13
    private const val ID_ENERGY = 14
    private const val ID_TELECOM = 15
    private const val ID_AUTOMOTIVE = 16
    private const val ID_HOSPITALITY = 17
    private const val ID_SPORT = 18
    private const val ID_CULTURE = 19
    private const val ID_SOCIAL = 20

    fun getMockIndustries(): List<FilterIndustry> {
        return listOf(
            FilterIndustry(ID_SOFTWARE, "Разработка программного обеспечения"),
            FilterIndustry(ID_IT, "Информационные технологии"),
            FilterIndustry(ID_FINANCE, "Финансы и банковское дело"),
            FilterIndustry(ID_MARKETING, "Маркетинг и реклама"),
            FilterIndustry(ID_EDUCATION, "Образование и наука"),
            FilterIndustry(ID_MEDICINE, "Медицина и фармацевтика"),
            FilterIndustry(ID_CONSTRUCTION, "Строительство и архитектура"),
            FilterIndustry(ID_TRANSPORT, "Транспорт и логистика"),
            FilterIndustry(ID_RETAIL, "Торговля и ритейл"),
            FilterIndustry(ID_MEDIA, "СМИ и журналистика"),
            FilterIndustry(ID_LAW, "Юриспруденция"),
            FilterIndustry(ID_DESIGN, "Дизайн и креатив"),
            FilterIndustry(ID_AGRICULTURE, "Сельское хозяйство"),
            FilterIndustry(ID_ENERGY, "Энергетика"),
            FilterIndustry(ID_TELECOM, "Телекоммуникации"),
            FilterIndustry(ID_AUTOMOTIVE, "Автомобильная промышленность"),
            FilterIndustry(ID_HOSPITALITY, "Гостиничный и ресторанный бизнес"),
            FilterIndustry(ID_SPORT, "Спорт и фитнес"),
            FilterIndustry(ID_CULTURE, "Культура и искусство"),
            FilterIndustry(ID_SOCIAL, "Социальная работа")
        )
    }
}
