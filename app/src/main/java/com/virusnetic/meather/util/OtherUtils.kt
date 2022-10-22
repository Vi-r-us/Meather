package com.virusnetic.meather.util

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.virusnetic.meather.R
import com.virusnetic.meather.activities.LocationsActivity
import com.virusnetic.meather.activities.MainActivity
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class OtherUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")
        fun dateTime(time: Int, zone: String, format: String = "EEE, MMMM d K:mm a"): String {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val sdf = SimpleDateFormat(format)
                val netDate = Date(time.toLong() * 1000)
                sdf.format(netDate)
            } else {
                // parse the time zone
                val zoneId = ZoneId.of(zone)
                // create a moment in time from the given timestamp (in seconds!)
                val instant = Instant.ofEpochSecond(time.toLong())
                // define a formatter using the given pattern and a Locale
                val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
                // then make the moment in time consider the zone and return the formatted String
                Log.i("Check", instant.atZone(zoneId).format(formatter))
                instant.atZone(zoneId).format(formatter)
            }
        }

        fun getWeatherIcon(id: Int, iconId: String): Int {
            return when (id) {
                in 200..202 -> R.drawable.thunderstorm_rain
                in 210..221 -> if (iconId.endsWith("d")) R.drawable.thunderstorm_day
                                    else R.drawable.thunderstorm_night
                in 230..231 -> R.drawable.thunderstorm
                in 300..321 -> if (iconId.endsWith("d")) R.drawable.drizzle_day
                                    else R.drawable.drizzle_night
                in 500..504 -> if (iconId.endsWith("d")) R.drawable.rain_day
                                    else R.drawable.rain_night
                511 -> R.drawable.snow
                in 520..531 -> if (iconId.endsWith("d")) R.drawable.shower_rain_day
                                    else R.drawable.shower_rain_night
                in 600..602 -> R.drawable.snow
                in 611..613 -> R.drawable.sleet
                in 615..622 -> R.drawable.blizzard
                800 -> if (iconId.endsWith("d")) R.drawable.clear_day
                        else R.drawable.clear_night
                801 -> if (iconId.endsWith("d")) R.drawable.partly_cloudy_day
                        else R.drawable.partly_cloudy_night
                in 802..804 -> R.drawable.cloudy
                else -> R.drawable.others
            }
        }

        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

    }
}