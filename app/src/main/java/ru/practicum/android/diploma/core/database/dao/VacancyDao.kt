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

//    @Query("SELECT * FROM Vacancy")
//    fun getAllVacanciesFromFavourites(): Flow<VacancyEntity>

    @Query("SELECT * FROM Vacancy")
    fun getAllVacanciesFromFavourites(): Flow<List<VacancyEntity>> // Мы получаем список избранных вакансий, думаю, что должно быть List

    @Delete
    suspend fun deleteVacancyFromFavourites(vacancyEntity: VacancyEntity)

    @Query("SELECT * FROM Vacancy WHERE id = :vacancyId")
    suspend fun getVacancyFromFavouritesById(
        vacancyId: String
    ): VacancyEntity? //получить одну вакансию по id
}
