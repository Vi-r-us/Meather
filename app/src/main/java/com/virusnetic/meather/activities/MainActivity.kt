package com.virusnetic.meather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.virusnetic.meather.WeatherApplication
import com.virusnetic.meather.databinding.ActivityMainBinding
import com.virusnetic.meather.models.LocationEntity
import com.virusnetic.meather.util.Constants
import com.virusnetic.meather.util.LocationUtils
import com.virusnetic.meather.viewmodels.MainViewModel
import com.virusnetic.meather.viewmodels.MainViewModelFactory

    class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as WeatherApplication).weatherRepository

        mainViewModel =
            ViewModelProvider(this,
                MainViewModelFactory(repository)
            )[MainViewModel::class.java]

        checkIfPrimaryLocationAvailable()
        ifPrimaryLocationNotAvailableUseCurrentLocation()

        binding.ibRefresh.setOnClickListener {
            checkIfPrimaryLocationAvailable()
        }

        binding.ibMap.setOnClickListener {
            val intent = Intent(this, LocationsActivity::class.java)
            startActivity(intent)
        }
        binding.ibSetting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkIfPrimaryLocationAvailable() {
        mainViewModel.primaryLocation.observe(this) {
            Log.i("My_Tag", "$it")
            if (it != null) {
                binding.tvCurrentLocation.text = it.city
                mainViewModel.getAPIData(this, it.lat, it.lon)
            } else {
                if (Constants.weatherResponse != null) {
                    // use previously saved data is no primary exist
                    binding.tvCurrentLocation.text = Constants.getPreferenceWeather()?.city.toString()
                } else {
                    // Get Current Location if can't find previous saved data
                    LocationUtils.getCurrentLocation(this)
                    Log.i("MyTag", "lat:")
                }
            }
        }
    }

    private fun ifPrimaryLocationNotAvailableUseCurrentLocation() {
        LocationUtils.latLngLiveData.observe(this) {
            if (it != null && mainViewModel.primaryLocation.value == null) {
                val address = LocationUtils.getAddressFromLocation(this, it)
                Log.i("MyTag", "lat: ${it.latitude} lon: ${it.longitude}")
                mainViewModel.insertLocation(
                    LocationEntity(
                        0,
                        address!!.locality,
                        address.adminArea,
                        address.getAddressLine(0),
                        it.latitude,
                        it.longitude,
                        false
                    )
                )
            }
        }
    }
}