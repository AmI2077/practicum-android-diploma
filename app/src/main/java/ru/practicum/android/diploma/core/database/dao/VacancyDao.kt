package ru.practicum.android.diploma.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VacancyDao {

    @Insert
    fun insertVacancyToFavourites()

    @Query("SELECT * FROM ")
    fun getAllVacanciesFromFavourites()

    @Delete
    fun deleteVacancyFromFavourites()
}
