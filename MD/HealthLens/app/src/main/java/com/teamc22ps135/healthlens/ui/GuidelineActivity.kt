package com.teamc22ps135.healthlens.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.*
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.databinding.ActivityGuidelineBinding

class GuidelineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuidelineBinding

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.not_getting_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            if (allPermissionsGranted()) {
                startCameraX()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityGuidelineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadGuideline()

        binding.iconBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSelfie.setOnClickListener {
            accessCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGalleryAndCrop()
        }
    }

    private fun loadGuideline() {
        val items = arrayOf(
            getString(R.string.rules1),
            getString(R.string.rules2),
            getString(R.string.rules3),
            getString(R.string.rules4),
            getString(R.string.rules5)
        )
        val builder = SpannableStringBuilder()
        items.forEach { item ->
            builder.append(
                " $item\n\n",
                BulletSpan(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.rulesGuideline.text = builder
    }

    private fun accessCamera() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startCameraX()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, DetectionActivity::class.java)
        startActivity(intent)
    }

    private fun startGalleryAndCrop() {
        launcherCropImage.launch(
            options {
                setImageSource(
                    includeCamera = false, includeGallery = true
                )
                setAspectRatio(aspectRatioX = 1, aspectRatioY = 1)
                setAllowRotation(allowRotation = false)
                setAllowFlipping(allowFlipping = false)
            }
        )
    }

    private val launcherCropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriImageCrop = result.uriContent

            val intent = Intent(this@GuidelineActivity, ReviewDetectActivity::class.java)
            intent.putExtra(KEY_URI_IMAGE, uriImageCrop)
            intent.putExtra(
                ReviewDetectActivity.KEY_RESULT_CODE,
                ReviewDetectActivity.GALLERY_RESULT
            )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val KEY_URI_IMAGE = "uri"
    }
}