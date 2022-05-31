package com.teamc22ps135.healthlens.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.data.pref.FirstInstallPreference
import com.teamc22ps135.healthlens.databinding.ActivityLandingBinding
import com.teamc22ps135.healthlens.viewmodel.LandingViewModel
import com.teamc22ps135.healthlens.viewmodel.ViewModelFactory

class LandingActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLandingBinding
    private lateinit var landingViewModel: LandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()

        binding.btnGetStarted.setOnClickListener {
            landingViewModel.saveState(state = false)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        landingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(FirstInstallPreference.getInstance(dataStore))
        )[LandingViewModel::class.java]
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_confirmation))
            setMessage(getString(R.string.message_want_logout))
            setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                finish()
            }
            setNegativeButton(getString(R.string.prompt_cancel)) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }
}