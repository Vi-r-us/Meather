package com.virusnetic.meather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.virusnetic.meather.api.WeatherService
import com.virusnetic.meather.db.LocationDatabase
import com.virusnetic.meather.models.LocationEntity
import com.virusnetic.meather.models.WeatherResponse

class WeatherRepository(private val weatherService: WeatherService, private val locationDatabase: LocationDatabase) {

    private val weatherLiveData = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse>
        get() = weatherLiveData

    val locations = locationDatabase.locationDao().getAllLocations()
    val primaryLocation = locationDatabase.locationDao().getPrimaryLocation()

    suspend fun insert(locationEntity: LocationEntity) {
        // If database is empty make the first one primary
        if (primaryLocation.value == null)
            locationEntity.isPrimary = true
        locationDatabase.locationDao().insertLocation(locationEntity)
    }
    suspend fun update(locationEntity: LocationEntity) {
        locationDatabase.locationDao().updateLocation(locationEntity)
    }
    suspend fun delete(locationEntity: LocationEntity) {
        locationDatabase.locationDao().deleteLocation(locationEntity)
    }
    suspend fun updateLocationToNotPrimary() {
        locationDatabase.locationDao().updateLocationToNotPrimary()
    }

    suspend fun getWeather(lat : Double,
                           lon : Double,
                           units : String?,
                           appid : String?) {

        val result = weatherService.getWeatherResponse(lat, lon, units, appid)
        if (result?.body() != null) {
            weatherLiveData.postValue(result.body())
        }

    }
}