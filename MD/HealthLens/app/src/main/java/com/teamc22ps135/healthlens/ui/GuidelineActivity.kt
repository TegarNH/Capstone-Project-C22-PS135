package com.teamc22ps135.healthlens.ui

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                    "Tidak mendapatkan permission.",
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

        binding.iconBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        val items = arrayOf(getString(R.string.rules1), getString(R.string.rules2), getString(R.string.rules3), getString(R.string.rules4), getString(R.string.rules5))
        val builder =  SpannableStringBuilder()
        items.forEach { item ->
            builder.append(
                " $item\n\n",
                BulletSpan(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.rulesGuideline.text = builder

        binding.btnSelfie.setOnClickListener {
            accessCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
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

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val intent = Intent(this@GuidelineActivity, ReviewDetectActivity::class.java)
            intent.putExtra("uri", selectedImg)
            intent.putExtra("resultCode", ReviewDetectActivity.GALLERY_RESULT)
            startActivity(intent)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}