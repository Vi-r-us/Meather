package com.virusnetic.meather.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.virusnetic.meather.databinding.ActivitySettingsBinding
import com.virusnetic.meather.util.Constants
import kotlinx.coroutines.*


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    //private var manager = ReviewManagerFactory.create(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        binding.llAboutWeather.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://openweathermap.org/"))
            startActivity(browserIntent)
        }
        binding.llAboutTeam.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Vi-r-us"))
            startActivity(browserIntent)
        }
        binding.llDarkTheme.setOnClickListener {
            Constants.prefAppSettings.edit().putBoolean("NightMode", true).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding.llLightTheme.setOnClickListener {
            Constants.prefAppSettings.edit().putBoolean("NightMode", false).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.llRate.setOnClickListener {
            //startReviewFlow()
        }

        if ( Constants.prefAppSettings.getBoolean("NightMode", false) ) {
            binding.ivLightThemeCheck.visibility = View.INVISIBLE
            binding.ivDarkThemeCheck.visibility = View.VISIBLE
        } else {
            binding.ivLightThemeCheck.visibility = View.VISIBLE
            binding.ivDarkThemeCheck.visibility = View.INVISIBLE
        }
    }

    /*
    private fun startReviewFlow() {
        GlobalScope.launch(Dispatchers.IO) {
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(this@SettingsActivity, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                }
            }
        }
    }
    */


}