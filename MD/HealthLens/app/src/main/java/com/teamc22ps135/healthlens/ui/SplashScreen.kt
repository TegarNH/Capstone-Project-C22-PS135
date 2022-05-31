package com.teamc22ps135.healthlens.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.data.pref.FirstInstallPreference
import com.teamc22ps135.healthlens.viewmodel.LandingViewModel
import com.teamc22ps135.healthlens.viewmodel.SplashViewModel
import com.teamc22ps135.healthlens.viewmodel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var landingViewModel: LandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.isLoading.value
            }
        }

        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        setupViewModel()

        Handler().postDelayed({
           stateApp()
        }, DELAY_TIME)
    }

    private fun setupViewModel() {
        landingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(FirstInstallPreference.getInstance(dataStore))
        )[LandingViewModel::class.java]
    }

    private fun stateApp() {
        landingViewModel.getStateFirstInstall().observe(this) { state ->
            if (state.isFirstInstall) {
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        private const val DELAY_TIME = 3000L   // Delayed for 3 seconds.
    }
}