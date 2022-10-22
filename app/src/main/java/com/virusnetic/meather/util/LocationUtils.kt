package com.virusnetic.meather.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Delay
import okhttp3.internal.wait
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class LocationUtils {
    companion object {
        private lateinit var mFusedLocationClient: FusedLocationProviderClient
        private var latLng = MutableLiveData<LatLng>()

        val latLngLiveData : LiveData<LatLng>
        get() = latLng

        // Get address from Latitude and Longitude
        fun getAddressFromLocation(context: Context?, latLng: LatLng): Address? {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList: List<Address>? =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addressList == null || addressList.isEmpty()) {
                return null
            }
            return addressList[0]
        }

        // Get Latitude and Longitude from address
        fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<Address>?
            var p1: LatLng? = null
            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }
                val location = address[0]
                p1 = LatLng(location.latitude, location.longitude)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return p1
        }

        private fun setClient(context: Context) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
        private fun isLocationEnabled(context: Context): Boolean {
            // This provides access to the system location services.
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

        private fun locationPermissionRequests(context: Context) {
            Dexter.withContext(context).withPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        Log.i("MyTag", "entered")
                        requestLocationData()
                    } else {
                        Toast.makeText(context,
                            "You have denied location permission. Please enabled them.",
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermission(context)
                }
            }).onSameThread().check()
        }

        // If permissions are not granted show Alert-Dialog
        private fun showRationalDialogForPermission(context: Context) {
            Toast.makeText(context,
                "." ,
                Toast.LENGTH_SHORT).show()
            val builder = MaterialAlertDialogBuilder(context)
            with(builder) {
                setTitle("Change Setting")
                setMessage("Its looks like you have turned off the " +
                        "permission required for this feature. It can be enabled under the Application's " +
                        "Setting.")
                setPositiveButton("GO TO SETTINGS") { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context.packageName  , null)
                        intent.data = uri
                        startActivity(context, intent, null)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }.create().show()
        }

        @SuppressLint("MissingPermission")
        private fun requestLocationData() {
            val myLocationRequest = LocationRequest.create ().apply {
                priority = Priority.PRIORITY_HIGH_ACCURACY
                interval = 1000
                numUpdates = 1
            }

            mFusedLocationClient.requestLocationUpdates(
                myLocationRequest, mLocationCallBack,
                Looper.myLooper()
            )
        }

        private val mLocationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                Log.i("MyTag", "entered")
                val mLastLocation: Location? = p0.lastLocation
                val mLatitude = mLastLocation!!.latitude
                Log.i("Current Latitude", "$mLatitude")

                val mLongitude = mLastLocation.longitude
                Log.i("Current Longitude", "$mLongitude")

                latLng.postValue(LatLng(mLatitude, mLongitude))
            }
        }

         fun getCurrentLocation (context: Context): LatLng? {
             setClient(context)

             if (isLocationEnabled(context)) {
                 Log.i("MyTag", "entered")
                 locationPermissionRequests(context)
             } else {
                 turnOnLocationToastAndIntent(context)
             }

             return latLng.value
         }


        private fun turnOnLocationToastAndIntent(context: Context) {
            Toast.makeText(context,
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(context, intent, null)
        }
    }
}