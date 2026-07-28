package ru.practicum.android.diploma.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.core.models.details.Address
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Employer
import ru.practicum.android.diploma.core.models.details.Employment
import ru.practicum.android.diploma.core.models.details.Experience
import ru.practicum.android.diploma.core.models.details.Salary
import ru.practicum.android.diploma.core.models.details.Schedule
import ru.practicum.android.diploma.core.models.filter.FilterArea
import ru.practicum.android.diploma.core.models.filter.FilterIndustry

class VacancyTypeConverter {
    val gson = Gson()

    @TypeConverter
    fun fromSalary(salary: Salary?): String? = gson.toJson(salary)

    @TypeConverter
    fun toSalary(json: String?): Salary? = gson.fromJson(json, Salary::class.java)

    @TypeConverter
    fun fromAddress(address: Address?): String? = gson.toJson(address)

    @TypeConverter
    fun toAddress(json: String?): Address? = gson.fromJson(json, Address::class.java)

    @TypeConverter
    fun fromExperience(exp: Experience?): String? = gson.toJson(exp)

    @TypeConverter
    fun toExperience(json: String?): Experience? = gson.fromJson(json, Experience::class.java)

    @TypeConverter
    fun fromSchedule(schedule: Schedule?): String? = gson.toJson(schedule)

    @TypeConverter
    fun toSchedule(json: String?): Schedule? = gson.fromJson(json, Schedule::class.java)

    @TypeConverter
    fun fromEmployment(employment: Employment?): String? = gson.toJson(employment)

    @TypeConverter
    fun toEmployment(json: String?): Employment? = gson.fromJson(json, Employment::class.java)

    @TypeConverter
    fun fromContacts(contacts: Contacts?): String? = gson.toJson(contacts)

    @TypeConverter
    fun toContacts(json: String?): Contacts? = gson.fromJson(json, Contacts::class.java)

    @TypeConverter
    fun fromEmployer(employer: Employer): String = gson.toJson(employer)

    @TypeConverter
    fun toEmployer(json: String): Employer = gson.fromJson(json, Employer::class.java)

    @TypeConverter
    fun fromArea(area: FilterArea): String = gson.toJson(area)

    @TypeConverter
    fun toArea(json: String): FilterArea = gson.fromJson(json, FilterArea::class.java)

    @TypeConverter
    fun fromIndustry(industry: FilterIndustry): String = gson.toJson(industry)

    @TypeConverter
    fun toIndustry(json: String): FilterIndustry = gson.fromJson(json, FilterIndustry::class.java)

    @TypeConverter
    fun fromSkillsList(skills: List<String>): String = gson.toJson(skills)

    @TypeConverter
    fun toSkillsList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}
