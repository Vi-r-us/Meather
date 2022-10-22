package com.virusnetic.meather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.virusnetic.meather.models.LocationEntity

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)

    @Delete
    suspend fun deleteLocation(locationEntity: LocationEntity)

    @Update
    suspend fun updateLocation(locationEntity: LocationEntity)

    @Query("UPDATE locations SET isPrimary = 0 WHERE isPrimary = 1")
    suspend fun updateLocationToNotPrimary()

    @Query("SELECT * FROM locations WHERE isPrimary=:isPrimary")
    fun getPrimaryLocation(isPrimary: Boolean = true): LiveData<LocationEntity>

    @Query("SELECT * FROM locations WHERE isPrimary = 0")
    fun getAllLocations() : LiveData<List<LocationEntity>>
}