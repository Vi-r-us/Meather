package com.virusnetic.meather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String,
    val state: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    var isPrimary: Boolean
)