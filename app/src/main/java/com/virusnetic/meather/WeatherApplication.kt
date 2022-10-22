package com.virusnetic.meather

import android.app.Application
import android.content.Context
import com.virusnetic.meather.api.RetrofitHelper
import com.virusnetic.meather.api.WeatherService
import com.virusnetic.meather.db.LocationDatabase
import com.virusnetic.meather.repository.WeatherRepository
import com.virusnetic.meather.util.Constants
import com.virusnetic.meather.util.LocationUtils

class WeatherApplication: Application() {

    lateinit var weatherRepository: WeatherRepository

    override fun onCreate() {
        super.onCreate()

        // Shared preferences
        Constants.initializePreferences(this)

        initialize()
    }

    private fun initialize() {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        val database = LocationDatabase.getDatabase(applicationContext)
        weatherRepository = WeatherRepository(weatherService, database)
    }
}