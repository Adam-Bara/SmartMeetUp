package com.example.smartmeetup.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class SmartMeetupDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: SmartMeetupDatabase? = null

        fun getDatabase(context: Context): SmartMeetupDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartMeetupDatabase::class.java,
                    "smartmeetup_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
