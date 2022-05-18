package com.teamc22ps135.healthlens.ui

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.databinding.ActivityGuidelineBinding
import com.teamc22ps135.healthlens.databinding.ActivityReviewDetectBinding
import com.teamc22ps135.healthlens.util.rotateBitmap
import kotlinx.coroutines.supervisorScope
import java.io.File

class ReviewDetectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetectBinding

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ReviewDetectActivity.REQUEST_CODE_PERMISSIONS) {
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

    private fun allPermissionsGranted() = ReviewDetectActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityReviewDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPost1.setOnClickListener {
            super.onBackPressed()
            finish()
//            accessCamera()
        }
        binding.btnPost2.setOnClickListener{
            //prosess
        }
    }

    private fun accessCamera() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                ReviewDetectActivity.REQUIRED_PERMISSIONS,
                ReviewDetectActivity.REQUEST_CODE_PERMISSIONS
            )
        } else {
            startCameraX()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, DetectionActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == ReviewDetectActivity.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isFrontCamera = it.data?.getBooleanExtra("isFrontCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isFrontCamera
            )
            // nampilin hasil gambar harusnya ke reviewdetection
            binding.ivImage.setImageBitmap(result)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
