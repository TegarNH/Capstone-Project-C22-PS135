package com.teamc22ps135.healthlens.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teamc22ps135.healthlens.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idDetection = intent.getStringExtra("idDetection")
        Toast.makeText(this, idDetection, Toast.LENGTH_SHORT).show()
    }
}