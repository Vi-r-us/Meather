package com.virusnetic.meather.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.virusnetic.meather.models.WeatherResponse
import org.json.JSONObject


object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/"
    const val APP_ID = "814a1927c3bf0f94d1383b11ac6088a9"
    const val UNITS = "metric"
    var API_CALLED_ONCE_SUCCESSFULLY: Boolean = false
    var weatherResponse: WeatherResponse? = null
    private lateinit var prefWeather: SharedPreferences
    lateinit var prefAppSettings: SharedPreferences

    fun initializePreferences(context: Context) {
        prefWeather = context.getSharedPreferences(APP_ID, MODE_PRIVATE)
        prefAppSettings = context.getSharedPreferences(APP_ID, MODE_PRIVATE)

        if (prefAppSettings.getBoolean("NightMode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun storePreferenceWeather(weatherResponse: WeatherResponse) {
        val gson = Gson()
        //Convert json object to string
        val json = gson.toJson(weatherResponse)
        //Store in the sharedPreference
        prefWeather.edit().putString("weather_response", json).apply()
        Log.i("MyTag", "$prefWeather")
    }
    fun getPreferenceWeather(): WeatherResponse? {
        val data = prefWeather.getString("weather_response", "")
        Log.i("MyTag", "$data")
        Log.i("MyTag", "${Gson().fromJson(data, WeatherResponse::class.java)}")
        return Gson().fromJson(data, WeatherResponse::class.java)
    }

}


