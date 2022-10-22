package com.virusnetic.meather.views

import android.animation.Animator
import android.content.Context
import android.location.Address
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.virusnetic.meather.R
import com.virusnetic.meather.WeatherApplication
import com.virusnetic.meather.models.LocationEntity
import com.virusnetic.meather.util.LocationUtils
import com.virusnetic.meather.util.ToolbarVisibilityHandler
import com.virusnetic.meather.viewmodels.MainViewModel
import com.virusnetic.meather.viewmodels.MainViewModelFactory

class SearchView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private var toolbarVisibilityHandler: ToolbarVisibilityHandler? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_search, this, true)

        findViewById<View>(R.id.open_search_button).setOnClickListener {
            // Set Location toolbar to INVISIBLE
            toolbarVisibilityHandler?.hideToolbar()
            openSearch()
        }
        findViewById<View>(R.id.close_search_button).setOnClickListener {
            closeSearch()
            // Set Location toolbar to VISIBLE
            toolbarVisibilityHandler?.showToolbar()
        }
        findViewById<View>(R.id.execute_search_button).setOnClickListener {
            val addressStr = findViewById<EditText>(R.id.search_input_text).text

            try {
                var latLng: LatLng? = null
                var address: Address? = null
                latLng = LocationUtils.getLocationFromAddress(context, addressStr.toString())
                address = LocationUtils.getAddressFromLocation(context, latLng!!)
                // Add Location to database
                toolbarVisibilityHandler?.insertRecord(LocationEntity(
                    0,
                    address!!.locality,
                    address.adminArea,
                    address.getAddressLine(0),
                    latLng.latitude,
                    latLng.longitude,
                    false
                ))

            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }

            closeSearch()
            // Set Location toolbar to VISIBLE
            toolbarVisibilityHandler?.showToolbar()
        }
    }

    fun setToolbarVisibilityHandler(handler: ToolbarVisibilityHandler?) {
        toolbarVisibilityHandler = handler
    }

    private fun openSearch() {
        val open_search_button: View = findViewById(R.id.open_search_button)
        val search_input_text: EditText = findViewById(R.id.search_input_text)
        val search_open_view: RelativeLayout = findViewById(R.id.search_open_view)

        search_input_text.setText("")
        search_open_view.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (open_search_button.right + open_search_button.left) / 2,
            (open_search_button.top + open_search_button.bottom) / 2,
            0f, width.toFloat()
        )
        circularReveal.duration = 300
        circularReveal.start()
    }

    private fun closeSearch() {
        val open_search_button: View = findViewById(R.id.open_search_button)
        val search_input_text: EditText = findViewById(R.id.search_input_text)
        val search_open_view: RelativeLayout = findViewById(R.id.search_open_view)

        val circularConceal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (open_search_button.right + open_search_button.left) / 2,
            (open_search_button.top + open_search_button.bottom) / 2,
            width.toFloat(), 0f
        )

        circularConceal.duration = 300
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                search_open_view.visibility = View.INVISIBLE
                search_input_text.setText("")
                circularConceal.removeAllListeners()
            }
        })

    }
}