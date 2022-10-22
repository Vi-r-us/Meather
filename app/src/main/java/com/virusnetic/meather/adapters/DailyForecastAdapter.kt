package com.virusnetic.meather.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.virusnetic.meather.R
import com.virusnetic.meather.models.WeatherResponse
import com.virusnetic.meather.util.OtherUtils
import kotlin.math.ceil
import kotlin.math.floor

class DailyForecastAdapter(val context: Context, private val weatherResponse: WeatherResponse) :
    RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var ivWeatherLogo: ImageView = itemView.findViewById(R.id.iv_weatherLogo)
        var tvHighestTemperature: TextView = itemView.findViewById(R.id.tv_highestTemperature)
        var tvLowestTemperature: TextView = itemView.findViewById(R.id.tv_lowestTemperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).
            inflate(R.layout.daily_forecast_list_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mPosition = (position) * 8
        holder.tvTime.text =
            OtherUtils.dateTime(weatherResponse.list[mPosition].dt, "Asia","dd MMM")
        holder.ivWeatherLogo.setBackgroundResource(
            OtherUtils.getWeatherIcon(weatherResponse.list[mPosition].weather[0].id,
                weatherResponse.list[mPosition].weather[0].icon))
        holder.tvLowestTemperature.text =
            floor(weatherResponse.list[mPosition].main.tempMin).toInt().toString() + "°C"
        holder.tvHighestTemperature.text =
            ceil(weatherResponse.list[mPosition].main.tempMax).toInt().toString() + "°C"
    }

    override fun getItemCount(): Int {
        return 5
    }


}