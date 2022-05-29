package com.teamc22ps135.healthlens.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Detection Result"

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idDetection = intent.getStringExtra("idDetection")
        Toast.makeText(this, idDetection, Toast.LENGTH_SHORT).show()

        binding.btnBackHome.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.btnDetectAgain.setOnClickListener {
            showDialogChooseDetection()
        }
    }

    private fun showDialogChooseDetection() {
        var selectedDetectionIndex = 0
        val detection = arrayOf(
            getString(R.string.title_skin_problems),
            getString(R.string.title_skin_type)
        )
        var selectedDetection = detection[selectedDetectionIndex]

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.tv_choose_detection))
            setSingleChoiceItems(detection, selectedDetectionIndex) { _, which ->
                selectedDetectionIndex = which
                selectedDetection = detection[which]
            }
            setPositiveButton("OK") { _, _ ->
                if (selectedDetection == "Skin Problems") {
                    saveStateChooseDetection(MainActivity.VALUE_SKIN_PROBLEMS)
                } else {
                    saveStateChooseDetection(MainActivity.VALUE_SKIN_TYPE)
                }

                val intent = Intent(this@ResultActivity, GuidelineActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(R.string.prompt_cancel)) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }

    private fun saveStateChooseDetection(state: String) {
        val preferences =
            getSharedPreferences(MainActivity.PREFS_CHOOSE_DETECTION, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(MainActivity.KEY_SKIN, state)
        editor.apply()
    }
}