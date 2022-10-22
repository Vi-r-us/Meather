package com.virusnetic.meather.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.virusnetic.meather.WeatherApplication
import com.virusnetic.meather.adapters.LocationAdapter
import com.virusnetic.meather.databinding.ActivityLocationsBinding
import com.virusnetic.meather.models.LocationEntity
import com.virusnetic.meather.util.LocationUtils
import com.virusnetic.meather.util.SwipeToDeleteCallback
import com.virusnetic.meather.util.SwipeToEditCallback
import com.virusnetic.meather.util.ToolbarVisibilityHandler
import com.virusnetic.meather.viewmodels.MainViewModel
import com.virusnetic.meather.viewmodels.MainViewModelFactory
import com.virusnetic.meather.views.SearchView

class LocationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationsBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as WeatherApplication).weatherRepository

        mainViewModel = ViewModelProvider(this,
                MainViewModelFactory(repository)
            )[MainViewModel::class.java]

        binding.ibCurrentLocations.setOnClickListener {
            val latLng = LocationUtils.getCurrentLocation(this)
            //Log.i("MyTag", "lat: $latLng")

            if (latLng != null) {
                val address = LocationUtils.getAddressFromLocation(this, latLng)
                Log.i("MyTag", "lat: ${latLng.latitude} lon: ${latLng.longitude}")
                mainViewModel.insertLocation(
                        LocationEntity(
                            0,
                            address!!.locality,
                            address.adminArea,
                            address.getAddressLine(0),
                            latLng.latitude,
                            latLng.longitude,
                            false
                        )
                )
            }
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        val toolbarView = binding.clToolBar
        binding.searchView.setToolbarVisibilityHandler(object: ToolbarVisibilityHandler {

            override fun showToolbar() {
                toolbarView.visibility = View.VISIBLE
            }

            override fun hideToolbar() {
                toolbarView.visibility = View.INVISIBLE
            }

            override fun insertRecord(locationEntity: LocationEntity) {
                mainViewModel.insertLocation(locationEntity)
            }

        })

        initRecyclerView()
        setupUI()


        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val newPrimaryLocation = adapter.getItem(viewHolder.adapterPosition)
                newPrimaryLocation.isPrimary = true

                mainViewModel.updateLocationToNotPrimary()
                mainViewModel.updateLocation(newPrimaryLocation)
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvLocations)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel.deleteLocation(adapter.getItem(viewHolder.adapterPosition))
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvLocations)
    }

    private fun initRecyclerView() {
        binding.rvLocations.layoutManager = LinearLayoutManager(this)
    }

    private fun setupUI() {
        // Setup recycler view
        mainViewModel.locations.observe(this) {
            // If list has only 1 location that means its primary only
            // adapter initialization not needed
            adapter = LocationAdapter(this, it)
            binding.rvLocations.adapter = adapter
            Log.i("MY_TAG", it.toString())
        }

        // Setup primary location
        mainViewModel.primaryLocation.observe(this) {
            if (it != null) {
                binding.clPrimaryLocation.visibility = View.VISIBLE
                binding.tvPrimaryCityState.text = "${it.city}, ${it.state}"
                binding.tvPrimaryFullAddress.text = it.address
            }
        }
    }

}