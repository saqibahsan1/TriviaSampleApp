package com.saqib.triviasample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.saqib.triviasample.model.Result

@Database(entities = [Result::class], exportSchema = false, version = 2)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val configurationDao: ConfigurationDao?

    companion object {
        private var dbInstance: AppDatabase? = null
        private var Lock = Any()
        operator fun invoke(context: Context) = dbInstance ?: synchronized(Lock) {
            dbInstance ?: buildDbInstance(context)
                    .also { dbInstance = it }
        }

        private fun buildDbInstance(context: Context) =
                Room.databaseBuilder(context,
                        AppDatabase::class.java, "trivia_db")
                        .fallbackToDestructiveMigration()
                        .build()
    }

}