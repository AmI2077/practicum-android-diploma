package ru.practicum.android.diploma.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.practicum.android.diploma.core.database.converters.VacancyTypeConverter
import ru.practicum.android.diploma.core.models.details.Address
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Employer
import ru.practicum.android.diploma.core.models.details.Employment
import ru.practicum.android.diploma.core.models.details.Experience
import ru.practicum.android.diploma.core.models.details.Salary
import ru.practicum.android.diploma.core.models.details.Schedule
import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry

@Entity(tableName = "Vacancy")
@TypeConverters(VacancyTypeConverter::class)
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
)
