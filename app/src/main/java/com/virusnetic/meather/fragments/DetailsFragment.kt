package com.virusnetic.meather.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.virusnetic.meather.R
import com.virusnetic.meather.WeatherApplication
import com.virusnetic.meather.adapters.DailyForecastAdapter
import com.virusnetic.meather.adapters.HourlyForecastAdapter
import com.virusnetic.meather.databinding.FragmentDetailsBinding
import com.virusnetic.meather.models.WeatherResponse
import com.virusnetic.meather.util.Constants
import com.virusnetic.meather.util.NetworkUtils
import com.virusnetic.meather.util.OtherUtils
import com.virusnetic.meather.viewmodels.MainViewModel
import com.virusnetic.meather.viewmodels.MainViewModelFactory


class DetailsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var dailyAdapter: DailyForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        val repository = (activity?.application as WeatherApplication).weatherRepository

        mainViewModel =
            ViewModelProvider(this,
                MainViewModelFactory(repository)
            )[MainViewModel::class.java]

        if(!NetworkUtils.isNetworkAvailable(requireContext())) { // Internet Not Available
            if (Constants.getPreferenceWeather() != null) {
                setupUI(Constants.getPreferenceWeather()!!)
                Log.i("MyTag", Constants.getPreferenceWeather()!!.toString())
            }
        }

        // Set weatherResponse for the app
        mainViewModel.weathers.observe(requireActivity()) {
            Log.d("MEATHER_APP", it.toString())
            setupUI(it)
            Constants.weatherResponse = it
        }

        // Set Forecast visible when Forecast button clicked
        binding.tvForecastButton.setOnClickListener {
            binding.llDetails.visibility = View.INVISIBLE
            binding.llForecast.visibility = View.VISIBLE
            binding.tvForecastButton.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.textColor))
            binding.tvDetailsButton.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.translucent))
        }

        binding.tvDetailsButton.setOnClickListener {
            binding.llDetails.visibility = View.VISIBLE
            binding.llForecast.visibility = View.INVISIBLE
            binding.tvForecastButton.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.translucent))
            binding.tvDetailsButton.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.textColor))
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(weatherResponse: WeatherResponse) {

        binding.tvPrecipitation.text = "${weatherResponse.list[0].pop*100} %"
        binding.tvSeWind.text =
            "${OtherUtils.roundOffDecimal(weatherResponse.list[0].wind.speed*3.6)} km/h"
        binding.tvHumidity.text = "${weatherResponse.list[0].main.humidity} %"
        binding.tvVisibility.text =
            "${OtherUtils.roundOffDecimal(weatherResponse.list[0].visibility/1000.0)} km"
        binding.tvCloudiness.text =
            when (weatherResponse.list[0].clouds.all) {
                in 0..20 -> "Lowest"
                in 21..40 -> "Low"
                in 41..60 -> "Medium"
                in 61..80 -> "High"
                else -> "Highest"
            }
        binding.tvPressure.text = "${weatherResponse.list[0].main.pressure} hPa"

        // Recycler Views
        if (isAdded && requireActivity() != null) {
            hourlyAdapter = HourlyForecastAdapter(requireContext(), weatherResponse)
            binding.rvHourlyForecast.adapter = hourlyAdapter
            binding.rvHourlyForecast.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

            dailyAdapter = DailyForecastAdapter(requireContext(), weatherResponse)
            binding.rvDailyForecast.adapter = dailyAdapter
            binding.rvDailyForecast.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

}