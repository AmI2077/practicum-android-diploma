package ru.practicum.android.diploma.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.database.entities.VacancyEntity

@Dao
interface VacancyDao {

    @Insert
    suspend fun insertVacancyToFavourites(vacancyEntity: VacancyEntity)

    @Query("SELECT * FROM Vacancy")
    fun getAllVacanciesFromFavourites(): Flow<VacancyEntity>

    @Delete
    suspend fun deleteVacancyFromFavourites(vacancyEntity: VacancyEntity)
}
