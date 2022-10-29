package com.virusnetic.meather.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.virusnetic.meather.R
import com.virusnetic.meather.util.Constants
import com.virusnetic.meather.util.LocationUtils
import com.virusnetic.meather.util.OtherUtils
import kotlin.math.floor

/**
 * Implementation of App Widget functionality.
 */
class MediumWidget : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.medium_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            Constants.initializePreferences(context)

            val weatherResponse = Constants.getPreferenceWeather()
            Log.i("Widget", "$weatherResponse")

            if (weatherResponse != null) {
                views.setTextViewText(R.id.tv_temp,
                    "${weatherResponse.list[0].main.temp.toInt()}°")
                views.setTextViewText(R.id.tv_day,
                    OtherUtils.dateTime(weatherResponse.list[0].dt, "Asia", "EEEE"))

                val address = LocationUtils.getAddressFromLocation(context,
                    LatLng(weatherResponse.city.coord.lat, weatherResponse.city.coord.lon))
                if (address != null) {
                    views.setTextViewText(R.id.tv_location, address.locality)
                } else {
                    views.setTextViewText(R.id.tv_location, weatherResponse.city.name)
                }

                views.setTextViewText(R.id.tv_wind,
                    "${OtherUtils.roundOffDecimal(weatherResponse.list[0].wind.speed*3.6)} km/h")
                views.setTextViewText(R.id.tv_humidity,
                    "${weatherResponse.list[0].main.humidity} %")
                views.setTextViewText(R.id.tv_high,
                    "${floor(weatherResponse.list[0].main.tempMax).toInt()}°")
                views.setTextViewText(R.id.tv_low,
                    "${floor(weatherResponse.list[0].main.tempMin).toInt()}°")

                views.setImageViewResource(R.id.iv_weather_logo,
                    OtherUtils.getWeatherIcon(weatherResponse.list[0].weather[0].id,
                        weatherResponse.list[0].weather[0].icon))
                views.setInt(R.id.iv_weather_logo, "setColorFilter",
                    Color.parseColor("#616161"))
                views.setTextViewText(R.id.tv_desc,
                    weatherResponse.list[0].weather[0].description.replaceFirstChar { it.uppercaseChar() })
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
