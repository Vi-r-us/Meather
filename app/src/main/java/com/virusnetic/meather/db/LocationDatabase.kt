package com.virusnetic.meather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.virusnetic.meather.models.LocationEntity

@Database(entities = [LocationEntity::class], version = 1)
abstract class LocationDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context,
                                LocationDatabase::class.java,
                                "locationDB")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}