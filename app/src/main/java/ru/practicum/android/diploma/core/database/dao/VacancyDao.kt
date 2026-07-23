package ru.practicum.android.diploma.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.database.entities.VacancyEntity

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancyToFavourites(vacancyEntity: VacancyEntity)

    @Query("SELECT * FROM Vacancy")
    fun getAllVacanciesFromFavourites(): Flow<List<VacancyEntity>>

    @Delete
    suspend fun deleteVacancyFromFavourites(vacancyEntity: VacancyEntity)

    @Query("SELECT * FROM Vacancy WHERE id = :vacancyId LIMIT 1")
    suspend fun getVacancyFromFavouritesById(
        vacancyId: String
    ): VacancyEntity?
}
