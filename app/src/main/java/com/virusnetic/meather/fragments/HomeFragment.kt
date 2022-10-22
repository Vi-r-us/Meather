package com.virusnetic.meather.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.virusnetic.meather.R
import com.virusnetic.meather.WeatherApplication
import com.virusnetic.meather.databinding.FragmentHomeBinding
import com.virusnetic.meather.models.WeatherResponse
import com.virusnetic.meather.util.Constants
import com.virusnetic.meather.util.NetworkUtils
import com.virusnetic.meather.util.OtherUtils
import com.virusnetic.meather.viewmodels.MainViewModel
import com.virusnetic.meather.viewmodels.MainViewModelFactory
import kotlin.math.ceil
import kotlin.math.floor

class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentHomeBinding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

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
            binding.tvInSync.text = "not in sync"
        } else {    // Internet Available
            binding.tvInSync.text = "in sync"
        }

        // Set weatherResponse for the app
        mainViewModel.weathers.observe(requireActivity()) {
            Log.d("MEATHER_APP", it.toString())
            setupUI(it)
            Constants.storePreferenceWeather(it)
        }

        binding.llTemperature.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
        }
        binding.ivWeatherLogo.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
        }

        return binding.root

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI(weatherResponse: WeatherResponse) {

        binding.tvWeatherDesc.text =
            weatherResponse.list[0].weather[0].description.replaceFirstChar { it.uppercaseChar() }
        binding.tvTemperature.text = weatherResponse.list[0].main.temp.toInt().toString()
        binding.tvLowestTemperature.text =
            floor(weatherResponse.list[0].main.tempMin).toInt().toString() + "°C"
        binding.tvHighestTemperature.text =
            ceil(weatherResponse.list[0].main.tempMax).toInt().toString() + "°C"
        binding.tvSunriseTime.text =
            OtherUtils.dateTime(weatherResponse.city.sunrise, "Asia","hh:mm a")
        binding.tvSunsetTime.text =
            OtherUtils.dateTime(weatherResponse.city.sunset,"Asia","hh:mm a")
        binding.tvDate.text =
            OtherUtils.dateTime(weatherResponse.list[0].dt, "Asia", "EEEE, dd MMMM yyyy")
        binding.ivWeatherLogo.setBackgroundResource(
            OtherUtils.getWeatherIcon(weatherResponse.list[0].weather[0].id,
                weatherResponse.list[0].weather[0].icon))

    }

}