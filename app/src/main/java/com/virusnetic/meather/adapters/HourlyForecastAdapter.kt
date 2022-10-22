package com.virusnetic.meather.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.virusnetic.meather.R
import com.virusnetic.meather.models.WeatherResponse
import com.virusnetic.meather.util.OtherUtils

class HourlyForecastAdapter(val context: Context, private val weatherResponse: WeatherResponse) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var ivWeatherLogo: ImageView = itemView.findViewById(R.id.iv_weatherLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).
                    inflate(R.layout.hourly_forecast_list_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0)
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.textColor))
        holder.tvTime.text =
            OtherUtils.dateTime(weatherResponse.list[position].dt, "Asia","HH:mm")
        holder.ivWeatherLogo.setBackgroundResource(
            OtherUtils.getWeatherIcon(weatherResponse.list[position].weather[0].id,
                weatherResponse.list[position].weather[0].icon))
    }

    override fun getItemCount(): Int {
        return 10
    }
}