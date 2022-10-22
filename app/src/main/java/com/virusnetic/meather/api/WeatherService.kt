package com.virusnetic.meather.api

import com.virusnetic.meather.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("2.5/forecast")
    suspend fun getWeatherResponse(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String?,
        @Query("appid") appid : String?
    ) : Response<WeatherResponse>?

}