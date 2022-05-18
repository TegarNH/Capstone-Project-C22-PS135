package com.teamc22ps135.healthlens.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.databinding.ActivityLandingBinding
import com.teamc22ps135.healthlens.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.mipmap.logo)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skinProblemsDetection.setOnClickListener {
            val intent = Intent(this, GuidelineActivity::class.java)
            startActivity(intent)
        }

        binding.skinTypeDetection.setOnClickListener {
            val intent = Intent(this, GuidelineActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_confirmation))
            setMessage(getString(R.string.message_want_logout))
            setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                super.onBackPressed()
                finish()
            }
            setNegativeButton(getString(R.string.prompt_cancel)) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}