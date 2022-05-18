package com.teamc22ps135.healthlens.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.teamc22ps135.healthlens.databinding.ActivityReviewDetectBinding
import com.teamc22ps135.healthlens.util.rotateBitmap
import java.io.File

class ReviewDetectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityReviewDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultCode = intent.getIntExtra("resultCode", 0)
        if (resultCode == GALLERY_RESULT) {
            setImageFromGallery()
        } else if (resultCode == CAMERA_X_RESULT) {
            setImageFromCamera()
        }

        binding.btnTryAgain.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.btnProcess.setOnClickListener{
            // Proses API
        }
    }

    private fun setImageFromCamera() {
        val myFile = intent.getSerializableExtra("picture") as File
        val isFrontCamera = intent.getBooleanExtra("isFrontCamera", true)

        val result = rotateBitmap(
            BitmapFactory.decodeFile(myFile.path),
            isFrontCamera
        )

        binding.ivImage.setImageBitmap(result)
    }

    private fun setImageFromGallery() {
        val selectedImg = intent.getParcelableExtra<Uri>("uri") as Uri

        binding.ivImage.setImageURI(selectedImg)
    }

    companion object {
        const val CAMERA_X_RESULT = 10
        const val GALLERY_RESULT = 20
    }
}
