package ru.practicum.android.diploma.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.core.database.dao.VacancyDao
import ru.practicum.android.diploma.core.database.entities.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getVacancyDao(): VacancyDao

    companion object {

        private const val DB_NAME = "app_room_db"

        fun createInstance(
            context: Context
        ): AppDatabase {
            return Room
                .databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME,
                )
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }
}
